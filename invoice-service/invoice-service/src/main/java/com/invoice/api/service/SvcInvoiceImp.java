package com.invoice.api.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.entity.Invoice;
import com.invoice.api.repository.RepoInvoice;
import com.invoice.commons.mapper.MapperInvoice;
import com.invoice.commons.util.JwtDecoder;
import com.invoice.exception.ApiException;
import com.invoice.exception.DBAccessException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

import com.invoice.api.dto.DtoProductOut;
import com.invoice.api.entity.CartItem;
import com.invoice.api.entity.InvoiceItem;
import com.invoice.api.repository.RepoCartItem;

@Service
public class SvcInvoiceImp implements SvcInvoice {
	
	@Autowired
    private RepoInvoice repo;
	
	@Autowired
	private JwtDecoder jwtDecoder;
	
	@Autowired
	MapperInvoice mapper;
	
	@Autowired
	private RepoCartItem repoCartItem;

	@Autowired
	private HttpServletRequest request;

	private final RestTemplate restTemplate = new RestTemplate();

	private final String productServiceUrl = "http://localhost:8081/product/";

	@Override
	public List<DtoInvoiceList> findAll() {
		try {
			if(jwtDecoder.isAdmin()) {
				return mapper.toDtoList(repo.findAll());
			}else {
				Integer user_id = jwtDecoder.getUserId();
				return mapper.toDtoList(repo.findAllByUserId(user_id));
			}
		}catch (DataAccessException e) {
	        throw new DBAccessException();
	    }
	}

	@Override
	public Invoice findById(String id) {
		try {
			Invoice invoice = repo.findById(id).get();
			if(!jwtDecoder.isAdmin()) {
				Integer user_id = jwtDecoder.getUserId();
				if(!invoice.getUser_id().equals(user_id)) {
					throw new ApiException(HttpStatus.FORBIDDEN, "El token no es válido para consultar esta factura");
				}
			}
			return invoice;
		}catch (DataAccessException e) {
	        throw new DBAccessException();
	    }catch (NoSuchElementException e) {
			throw new ApiException(HttpStatus.NOT_FOUND, "El id de la factura no existe");
	    }
	}

	@Override
	public ApiResponse create() {
	    try {
	        Integer userId = jwtDecoder.getUserId();

	        List<CartItem> cartItems = repoCartItem.findByUserIdAndStatus(userId, 1);

	        if (cartItems == null || cartItems.isEmpty()) {
	            throw new ApiException(HttpStatus.BAD_REQUEST, "El carrito está vacío");
	        }

	        String token = request.getHeader("Authorization");

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", token);
	        HttpEntity<Void> entity = new HttpEntity<>(headers);

	        String invoiceId = UUID.randomUUID().toString();

	        Double total = 0.0;
	        Double taxes = 0.0;
	        Double subtotal = 0.0;

	        List<InvoiceItem> invoiceItems = new ArrayList<>();

	        for (CartItem cartItem : cartItems) {

	            DtoProductOut product;

	            try {
	                ResponseEntity<DtoProductOut> response = restTemplate.exchange(
	                        productServiceUrl + cartItem.getProductId(),
	                        HttpMethod.GET,
	                        entity,
	                        DtoProductOut.class
	                );

	                product = response.getBody();

	            } catch (RestClientException e) {
	                throw new ApiException(HttpStatus.NOT_FOUND,
	                        "El producto " + cartItem.getProduct() + " no existe");
	            }

	            if (product == null || product.getProduct_id() == null) {
	                throw new ApiException(HttpStatus.NOT_FOUND,
	                        "El producto " + cartItem.getProduct() + " no existe");
	            }

	            if (product.getStock() < cartItem.getQuantity()) {
	                throw new ApiException(HttpStatus.BAD_REQUEST,
	                        "No hay stock suficiente para el producto: " + product.getProduct());
	            }

	            Double itemTotal = product.getPrice() * cartItem.getQuantity();
	            Double itemTaxes = itemTotal * 0.16;
	            Double itemSubtotal = itemTotal - itemTaxes;

	            total += itemTotal;
	            taxes += itemTaxes;
	            subtotal += itemSubtotal;

	            InvoiceItem invoiceItem = new InvoiceItem();
	            invoiceItem.setInvoice_item_id(UUID.randomUUID().toString());
	            invoiceItem.setInvoice_id(invoiceId);
	            invoiceItem.setGtin(product.getGtin());
	            invoiceItem.setQuantity(cartItem.getQuantity());
	            invoiceItem.setUnit_price(product.getPrice());
	            invoiceItem.setSubtotal(itemSubtotal);
	            invoiceItem.setTaxes(itemTaxes);
	            invoiceItem.setTotal(itemTotal);

	            invoiceItems.add(invoiceItem);

	            // Actualizar stock en product-service
	            product.setStock(product.getStock() - cartItem.getQuantity());

	            HttpEntity<DtoProductOut> updateEntity = new HttpEntity<>(product, headers);

	            restTemplate.exchange(
	                    productServiceUrl + product.getProduct_id(),
	                    HttpMethod.PUT,
	                    updateEntity,
	                    String.class
	            );
	        }

	        Invoice invoice = new Invoice();
	        invoice.setInvoice_id(invoiceId);
	        invoice.setUser_id(userId);
	        invoice.setCreated_at(LocalDateTime.now().toString());
	        invoice.setSubtotal(subtotal);
	        invoice.setTaxes(taxes);
	        invoice.setTotal(total);
	        invoice.setItems(invoiceItems);

	        repo.save(invoice);

	        // Vaciar carrito
	        for (CartItem cartItem : cartItems) {
	            cartItem.setStatus(0);
	        }

	        repoCartItem.saveAll(cartItems);

	        return new ApiResponse("La factura ha sido registrada");

	    } catch (DataAccessException e) {
	        throw new DBAccessException();
	    }
	}
}

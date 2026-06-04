package com.invoice.api.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCartItemIn;
import com.invoice.api.dto.DtoProductOut;
import com.invoice.api.entity.CartItem;
import com.invoice.api.repository.RepoCartItem;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class SvcCartItemImp implements SvcCartItem {

    @Autowired
    RepoCartItem repo;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String productServiceUrl = "http://localhost:8081/product/";

    private Integer getUserId() {
        return 1;
    }

    @Override
    public ApiResponse create(DtoCartItemIn in) {
        Integer userId = getUserId();

        DtoProductOut product;
        try {
        	String token = request.getHeader("Authorization");

        	HttpHeaders headers = new HttpHeaders();
        	headers.set("Authorization", token);

        	HttpEntity<Void> entity = new HttpEntity<>(headers);

        	ResponseEntity<DtoProductOut> response = restTemplate.exchange(
        	        productServiceUrl + in.getProduct_id(),
        	        HttpMethod.GET,
        	        entity,
        	        DtoProductOut.class
        	);

        	product = response.getBody();
        } catch (RestClientException e) {
            return new ApiResponse("El producto no existe");
        }

        if (product == null || product.getProduct_id() == null) {
            return new ApiResponse("El producto no existe");
        }

        if (product.getStock() < in.getQuantity()) {
            return new ApiResponse("No hay stock suficiente");
        }

        CartItem existing = repo.findByUserIdAndProductIdAndStatus(userId, in.getProduct_id(), 1);

        if (existing != null) {
            Integer newQuantity = existing.getQuantity() + in.getQuantity();

            if (product.getStock() < newQuantity) {
                return new ApiResponse("No hay stock suficiente");
            }

            existing.setQuantity(newQuantity);
            repo.save(existing);
            return new ApiResponse("La cantidad del producto ha sido actualizada");
        }

        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId(product.getProduct_id());
        item.setGtin(product.getGtin());
        item.setProduct(product.getProduct());
        item.setUnitPrice(product.getPrice());
        item.setQuantity(in.getQuantity());
        item.setStatus(1);

        repo.save(item);

        return new ApiResponse("El producto ha sido agregado al carrito");
    }

    @Override
    public List<CartItem> findAll() {
        return repo.findByUserIdAndStatus(getUserId(), 1);
    }

    @Override
    public ApiResponse deleteById(Integer id) {
        CartItem item = repo.findById(id).orElse(null);

        if (item == null || item.getStatus() == 0) {
            return new ApiResponse("El artículo no existe en el carrito");
        }

        item.setStatus(0);
        repo.save(item);

        return new ApiResponse("El artículo ha sido eliminado del carrito");
    }

    @Override
    public ApiResponse deleteAll() {
        List<CartItem> items = repo.findByUserIdAndStatus(getUserId(), 1);

        for (CartItem item : items) {
            item.setStatus(0);
        }

        repo.saveAll(items);

        return new ApiResponse("El carrito ha sido vaciado");
    }
    
    @Autowired
    private HttpServletRequest request;
}
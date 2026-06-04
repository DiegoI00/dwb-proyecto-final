package com.product.common.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.product.api.dto.in.DtoProductIn;
import com.product.api.dto.out.DtoProductListOut;
import com.product.api.dto.out.DtoProductOut;
import com.product.api.entity.Product;

@Service
public class MapperProduct {

    public List<DtoProductListOut> fromProductList(List<Product> products){
        List<DtoProductListOut> list = new ArrayList<>();
        for(Product product: products) {
            list.add(new DtoProductListOut(
                    product.getProduct_id(),
                    product.getGtin(),
                    product.getProduct(),
                    product.getPrice(),
                    product.getStatus()
            ));
        }
        return list;
    }

    public Product fromDto(DtoProductIn dto) {
        Product product = new Product();
        product.setGtin(dto.getGtin());
        product.setProduct(dto.getProduct());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory_id(dto.getCategory_id());
        product.setStatus(1);
        return product;
    }

    public Product fromDto(Integer id, DtoProductIn dto) {
        Product product = fromDto(dto);
        product.setProduct_id(id);
        return product;
    }

    public DtoProductOut fromProduct(Product product) {
        DtoProductOut out = new DtoProductOut();
        out.setProduct_id(product.getProduct_id());
        out.setGtin(product.getGtin());
        out.setProduct(product.getProduct());
        out.setDescription(product.getDescription());
        out.setPrice(product.getPrice());
        out.setStock(product.getStock());
        out.setCategory_id(product.getCategory_id());
        out.setStatus(product.getStatus());
        return out;
    }
}

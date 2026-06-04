package com.product.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.in.DtoProductIn;
import com.product.api.dto.out.DtoProductListOut;
import com.product.api.dto.out.DtoProductOut;

public interface SvcProduct {

    ResponseEntity<List<DtoProductListOut>> getProducts();
    ResponseEntity<DtoProductOut> getProduct(Integer id);
    ResponseEntity<String> createProduct(DtoProductIn in);
    ResponseEntity<String> updateProduct(Integer id, DtoProductIn in);
    ResponseEntity<String> enableProduct(Integer id);
    ResponseEntity<String> disableProduct(Integer id);
}

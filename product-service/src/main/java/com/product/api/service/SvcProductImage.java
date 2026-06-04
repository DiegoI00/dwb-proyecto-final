package com.product.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.dto.out.DtoProductImageOut;

public interface SvcProductImage {

    List<DtoProductImageOut> getProductImages(Integer productId);

    ResponseEntity<String> createProductImage(Integer productId, DtoProductImageIn in);

    ResponseEntity<String> deleteProductImage(Integer productId, Integer productImageId);
}
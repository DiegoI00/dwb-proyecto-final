package com.invoice.api.service;

import java.util.List;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCartItemIn;
import com.invoice.api.entity.CartItem;

public interface SvcCartItem {

    ApiResponse create(DtoCartItemIn in);

    List<CartItem> findAll();

    ApiResponse deleteById(Integer id);

    ApiResponse deleteAll();
}
package com.invoice.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCartItemIn;
import com.invoice.api.entity.CartItem;
import com.invoice.api.service.SvcCartItem;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart-item")
public class CtrlCartItem {

    @Autowired
    SvcCartItem svc;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody DtoCartItemIn in) {
        return ResponseEntity.ok(svc.create(in));
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> findAll() {
        return ResponseEntity.ok(svc.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.deleteById(id));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteAll() {
        return ResponseEntity.ok(svc.deleteAll());
    }
}

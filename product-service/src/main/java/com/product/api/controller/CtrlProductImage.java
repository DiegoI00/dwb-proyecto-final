package com.product.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.dto.out.DtoProductImageOut;
import com.product.api.service.SvcProductImage;

import jakarta.validation.Valid;

// Swagger imports
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Product Image", description = "Gestión de imágenes de productos")
@RestController
@RequestMapping("/product/{id}/image")
public class CtrlProductImage {

    private final SvcProductImage svc;

    public CtrlProductImage(SvcProductImage svc) {
        this.svc = svc;
    }

    @Operation(
        summary = "Consultar imágenes de producto",
        description = "Consulta las imágenes asociadas a un producto mediante su ID"
    )
    @GetMapping
    public ResponseEntity<List<DtoProductImageOut>> getProductImages(@PathVariable("id") Integer productId) {
        return ResponseEntity.ok(svc.getProductImages(productId));
    }

    @Operation(
        summary = "Registrar imagen de producto",
        description = "Registra una nueva imagen para un producto"
    )
    @PostMapping
    public ResponseEntity<String> createProductImage(@PathVariable("id") Integer productId,
            @Valid @RequestBody DtoProductImageIn in) {
        return svc.createProductImage(productId, in);
    }

    @Operation(
        summary = "Eliminar imagen de producto",
        description = "Elimina una imagen específica de un producto"
    )
    @DeleteMapping("/{productImageId}")
    public ResponseEntity<String> deleteProductImage(@PathVariable("id") Integer productId,
            @PathVariable Integer productImageId) {
        return svc.deleteProductImage(productId, productImageId);
    }
}
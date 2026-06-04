package com.product.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.product.api.dto.in.DtoProductIn;
import com.product.api.dto.out.DtoProductListOut;
import com.product.api.dto.out.DtoProductOut;
import com.product.api.service.SvcProduct;

import jakarta.validation.Valid;

// Swagger imports
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Product", description = "Administración de productos")
@RestController
@RequestMapping("/product")
public class CtrlProduct {

    @Autowired
    SvcProduct svc;

    @Operation(
        summary = "Consultar productos",
        description = "Lista todos los productos registrados en el sistema"
    )
    @GetMapping
    public ResponseEntity<List<DtoProductListOut>> getProducts() {
        return svc.getProducts();
    }

    @Operation(
        summary = "Consultar producto",
        description = "Consulta el detalle de un producto mediante su ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<DtoProductOut> getProduct(@PathVariable Integer id) {
        return svc.getProduct(id);
    }

    @Operation(
        summary = "Registrar producto",
        description = "Registra un nuevo producto en el sistema"
    )
    @PostMapping
    public ResponseEntity<String> createProduct(@Valid @RequestBody DtoProductIn in) {
        return svc.createProduct(in);
    }

    @Operation(
        summary = "Actualizar producto",
        description = "Actualiza un producto existente mediante su ID"
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id,
            @Valid @RequestBody DtoProductIn in) {
        return svc.updateProduct(id, in);
    }

    @Operation(
        summary = "Activar producto",
        description = "Activa un producto existente"
    )
    @PatchMapping("/{id}/enable")
    public ResponseEntity<String> enableProduct(@PathVariable Integer id) {
        return svc.enableProduct(id);
    }

    @Operation(
        summary = "Desactivar producto",
        description = "Desactiva un producto existente"
    )
    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> disableProduct(@PathVariable Integer id) {
        return svc.disableProduct(id);
    }
}
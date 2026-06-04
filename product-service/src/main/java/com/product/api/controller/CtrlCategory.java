package com.product.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.service.SvcCategory;

import jakarta.validation.Valid;

// Swagger imports
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "Category", description = "Catálogo de categorías")
@RestController
@RequestMapping("/category")
public class CtrlCategory {

    private final SvcCategory svcCategory;

    public CtrlCategory(SvcCategory svcCategory) {
        this.svcCategory = svcCategory;
    }

    @Operation(
        summary = "Consultar categorías",
        description = "Lista todas las categorías registradas en el sistema"
    )
    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(svcCategory.findAll());
    }

    @Operation(
        summary = "Consultar categorías activas",
        description = "Lista las categorías activas registradas en el sistema"
    )
    @GetMapping("/active")
    public ResponseEntity<List<Category>> findActive() {
        return ResponseEntity.ok(svcCategory.findActive());
    }

    @Operation(
        summary = "Registrar categoría",
        description = "Registra una nueva categoría en el sistema"
    )
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody DtoCategoryIn in) {
        return svcCategory.create(in);
    }

    @Operation(
        summary = "Actualizar categoría",
        description = "Actualiza una categoría existente mediante su ID"
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id,
                                         @Valid @RequestBody DtoCategoryIn in) {
        return svcCategory.update(in, id);
    }

    @Operation(
        summary = "Activar categoría",
        description = "Activa una categoría existente"
    )
    @PatchMapping("/{id}/enable")
    public ResponseEntity<String> enable(@PathVariable Integer id) {
        return svcCategory.enable(id);
    }

    @Operation(
        summary = "Desactivar categoría",
        description = "Desactiva una categoría existente"
    )
    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> disable(@PathVariable Integer id) {
        return svcCategory.disable(id);
    }
}
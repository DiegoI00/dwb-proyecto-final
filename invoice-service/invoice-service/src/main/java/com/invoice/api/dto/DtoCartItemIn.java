package com.invoice.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class DtoCartItemIn {

    @NotNull(message = "El product_id es obligatorio")
    private Integer product_id;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer quantity;

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

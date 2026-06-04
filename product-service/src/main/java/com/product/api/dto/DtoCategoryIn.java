
package com.product.api.dto;

// Para ver que no sean nulos
import jakarta.validation.constraints.NotNull;


public class DtoCategoryIn {

    @NotNull(message = "El nombre de la categoría es obligatorio")
    private String category;

    @NotNull(message = "El tag de la categoría es obligatorio")
    private String tag;

    public DtoCategoryIn() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

package com.product.api.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class DtoProductImageIn {

    @JsonProperty("image")
    @NotNull(message = "El image es obligatorio")
    private String image;

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}

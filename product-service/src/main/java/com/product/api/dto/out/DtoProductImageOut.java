package com.product.api.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoProductImageOut {

    @JsonProperty("product_image_id")
    private Integer productImageId;

    @JsonProperty("product_id")
    private Integer productId;

    @JsonProperty("image")
    private String image;

    public DtoProductImageOut(Integer productImageId, Integer productId, String image) {
        this.productImageId = productImageId;
        this.productId = productId;
        this.image = image;
    }

    public Integer getProductImageId() { return productImageId; }
    public Integer getProductId() { return productId; }
    public String getImage() { return image; }
}

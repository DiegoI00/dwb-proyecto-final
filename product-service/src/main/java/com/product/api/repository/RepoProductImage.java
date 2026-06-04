package com.product.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.api.entity.ProductImage;

@Repository
public interface RepoProductImage extends JpaRepository<ProductImage, Integer> {

    List<ProductImage> findByProductIdAndStatus(Integer productId, Integer status);

    ProductImage findByProductImageIdAndProductId(Integer productImageId, Integer productId);
}
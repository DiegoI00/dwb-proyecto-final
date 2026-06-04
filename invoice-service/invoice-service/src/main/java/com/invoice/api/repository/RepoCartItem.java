package com.invoice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.invoice.api.entity.CartItem;

public interface RepoCartItem extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByUserIdAndStatus(Integer userId, Integer status);

    CartItem findByUserIdAndProductIdAndStatus(Integer userId, Integer productId, Integer status);
}
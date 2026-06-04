

package com.product.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.api.entity.Category;

public interface RepoCategory extends JpaRepository<Category, Integer> {

    List<Category> findByStatusOrderByCategoryAsc(Integer status);

    boolean existsByCategory(String category);

    boolean existsByTag(String tag);

    boolean existsByCategoryAndCategoryIdNot(String category, Integer categoryId);

    boolean existsByTagAndCategoryIdNot(String tag, Integer categoryId);
}
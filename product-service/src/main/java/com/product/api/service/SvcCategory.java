
package com.product.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;

public interface SvcCategory {

    List<Category> findAll();

    List<Category> findActive();

    ResponseEntity<String> create(DtoCategoryIn in);

    ResponseEntity<String> update(DtoCategoryIn in, Integer id);

    ResponseEntity<String> enable(Integer id);

    ResponseEntity<String> disable(Integer id);
}
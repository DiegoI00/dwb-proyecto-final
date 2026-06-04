package com.product.api.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;
import com.product.exception.ApiException;

@Service
public class SvcCategoryImpl implements SvcCategory {

    private final RepoCategory repo;

    public SvcCategoryImpl(RepoCategory repo) {
        this.repo = repo;
    }

    @Override
    public List<Category> findAll() {
        try {
            return repo.findAll();
        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al consultar las categorías en la base de datos");
        }
    }

    @Override
    public List<Category> findActive() {
        try {
            return repo.findByStatusOrderByCategoryAsc(1);
        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al consultar las categorías activas en la base de datos");
        }
    }

    @Override
    public ResponseEntity<String> create(DtoCategoryIn in) {
        try {
            if (repo.existsByCategory(in.getCategory())) {
                throw new ApiException(HttpStatus.CONFLICT,
                        "El nombre de la categoría ya está registrado");
            }

            if (repo.existsByTag(in.getTag())) {
                throw new ApiException(HttpStatus.CONFLICT,
                        "El tag de la categoría ya está registrado");
            }

            Category category = new Category();
            category.setCategory(in.getCategory());
            category.setTag(in.getTag());
            category.setStatus(1);

            repo.save(category);

            return ResponseEntity.ok("La categoría ha sido registrada");
        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al registrar la categoría en la base de datos");
        }
    }

    @Override
    public ResponseEntity<String> update(DtoCategoryIn in, Integer id) {
        try {
            Category category = validateCategoryId(id);

            if (repo.existsByCategoryAndCategoryIdNot(in.getCategory(), id)) {
                throw new ApiException(HttpStatus.CONFLICT,
                        "El nombre de la categoría ya está registrado");
            }

            if (repo.existsByTagAndCategoryIdNot(in.getTag(), id)) {
                throw new ApiException(HttpStatus.CONFLICT,
                        "El tag de la categoría ya está registrado");
            }

            category.setCategory(in.getCategory());
            category.setTag(in.getTag());

            repo.save(category);

            return ResponseEntity.ok("La categoría ha sido actualizada");
        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al actualizar la categoría en la base de datos");
        }
    }

    @Override
    public ResponseEntity<String> enable(Integer id) {
        try {
            Category category = validateCategoryId(id);
            category.setStatus(1);
            repo.save(category);
            return ResponseEntity.ok("La categoría ha sido activada");
        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al activar la categoría en la base de datos");
        }
    }

    @Override
    public ResponseEntity<String> disable(Integer id) {
        try {
            Category category = validateCategoryId(id);
            category.setStatus(0);
            repo.save(category);
            return ResponseEntity.ok("La categoría ha sido desactivada");
        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al desactivar la categoría en la base de datos");
        }
    }

    private Category validateCategoryId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "El id de la categoría no existe"));
    }
}
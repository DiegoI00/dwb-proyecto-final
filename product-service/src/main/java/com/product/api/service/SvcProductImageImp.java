package com.product.api.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.dto.out.DtoProductImageOut;
import com.product.api.entity.Product;
import com.product.api.entity.ProductImage;
import com.product.api.repository.RepoProduct;
import com.product.api.repository.RepoProductImage;
import com.product.exception.ApiException;

@Service
public class SvcProductImageImp implements SvcProductImage {

    private final RepoProduct repoProduct;
    private final RepoProductImage repoProductImage;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.images}")
    private String uploadImages;

    public SvcProductImageImp(RepoProduct repoProduct, RepoProductImage repoProductImage) {
        this.repoProduct = repoProduct;
        this.repoProductImage = repoProductImage;
    }

    @Override
    public List<DtoProductImageOut> getProductImages(Integer productId) {
        try {
            validateProductId(productId);

            List<ProductImage> images = repoProductImage.findByProductIdAndStatus(productId, 1);
            List<DtoProductImageOut> out = new ArrayList<>();

            for (ProductImage image : images) {
                out.add(new DtoProductImageOut(
                        image.getProductImageId(),
                        image.getProductId(),
                        readProductImageFile(image.getImage())
                ));
            }

            return out;
        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al consultar las imágenes del producto");
        }
    }

    @Override
    public ResponseEntity<String> createProductImage(Integer productId, DtoProductImageIn in) {
        try {
            validateProductId(productId);

            if (in.getImage().startsWith("data:image")) {
                int commaIndex = in.getImage().indexOf(",");
                if (commaIndex != -1) {
                    in.setImage(in.getImage().substring(commaIndex + 1));
                }
            }

            byte[] imageBytes = Base64.getDecoder().decode(in.getImage());
            String fileName = UUID.randomUUID().toString() + ".png";
            Path imagePath = Paths.get(uploadDir, uploadImages, "product", fileName);

            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageBytes);

            ProductImage productImage = new ProductImage();
            productImage.setProductId(productId);
            productImage.setImage("/img/product/" + fileName);
            productImage.setStatus(1);

            repoProductImage.save(productImage);

            return new ResponseEntity<>("La imagen ha sido registrada", HttpStatus.CREATED);
        } catch (IOException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo");
        } catch (IllegalArgumentException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La imagen no tiene un Base64 válido");
        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al registrar la imagen del producto en la base de datos");
        }
    }

    @Override
    public ResponseEntity<String> deleteProductImage(Integer productId, Integer productImageId) {
        try {
            validateProductId(productId);

            ProductImage productImage = repoProductImage.findByProductImageIdAndProductId(productImageId, productId);
            if (productImage == null) {
                throw new ApiException(HttpStatus.NOT_FOUND,
                        "El id de la imagen del producto no existe");
            }

            productImage.setStatus(0);
            repoProductImage.save(productImage);

            return new ResponseEntity<>("La imagen ha sido eliminada", HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar la imagen del producto en la base de datos");
        }
    }

    private void validateProductId(Integer id) {
        Product product = repoProduct.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "El id del producto no existe"));
    }

    private String readProductImageFile(String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isBlank()) {
                return "";
            }

            if (imageUrl.startsWith("/")) {
                imageUrl = imageUrl.substring(1);
            }

            Path imagePath = Paths.get(uploadDir, imageUrl);

            if (!Files.exists(imagePath)) {
                return "";
            }

            byte[] imageBytes = Files.readAllBytes(imagePath);
            return Base64.getEncoder().encodeToString(imageBytes);

        } catch (IOException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al leer el archivo");
        }
    }
}

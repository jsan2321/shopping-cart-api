package com.ecoapi.goodshopping.repository;

import com.ecoapi.goodshopping.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// provides standard CRUD (Create, Read, Update, Delete) operations for the Image entity
public interface ImageRepository extends JpaRepository<Image, Long> {
    // derived query method that retrieves a list of Image entities based on field of the Product entity associated with the Image
    List<Image> findByProductId(Long id);
}

package com.ecoapi.goodshopping.repository;

import com.ecoapi.goodshopping.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String category);

    // The words after the "findBy" prefix are interpreted as properties of the Product entity

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByName(String name);

    List<Product> findByBrandAndName(String brand, String name);

    // check if a record (at least) exists in the database that matches the specified name and brand values
    boolean existsByNameAndBrand(String name, String brand);
}

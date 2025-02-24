package com.ecoapi.goodshopping.repository;

import com.ecoapi.goodshopping.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Defining Custom Query Method... it refers to the name property of the Category entity associated with the Product
    // Spring Data JPA automatically implements this method based on the method name and the entity relationships
    // similar query: SELECT * FROM Product p JOIN Category c ON p.category_id = c.id WHERE c.name = 'name';
    List<Product> findByCategoryName(String category);

    // The words after the "findBy" prefix are interpreted as properties of the Product entity

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByName(String name);

    List<Product> findByBrandAndName(String brand, String name);

    // The two parameters are used to filter the entities based on the brand and name properties
    // It counts the number of entities where the brand property matches the provided brand parameter and the name property matches the provided name parameter
    // If no entities match the criteria, the method will return 0
    // Similar Query: SELECT COUNT(*) FROM Product p WHERE p.brand = 'Nike' AND p.name = 'Running Shoes';
    Long countByBrandAndName(String brand, String name);
}

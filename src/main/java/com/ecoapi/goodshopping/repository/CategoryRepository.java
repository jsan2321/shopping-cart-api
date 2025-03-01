package com.ecoapi.goodshopping.repository;

import com.ecoapi.goodshopping.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    // existsBy: Indicates that the method checks for existence.
    //Name: Refers to the name field of the Category entity.
    // SQL query like: SELECT COUNT(*) > 0 FROM category WHERE name = ?;
    boolean existsByName(String name);
}

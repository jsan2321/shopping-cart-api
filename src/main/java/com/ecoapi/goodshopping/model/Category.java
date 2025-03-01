package com.ecoapi.goodshopping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // Spring Boot uses Jackson by default to convert objects to JSON, and it has a depth limit.
    // When Jackson attempts to serialize a Product, it also serializes its Category, and within that Category, it attempts to serialize its list of Products, which again serializes Product, repeating the cycle indefinitely.
    @JsonIgnore // Ignore this field during serialization, avoiding infinite serialization (JSON with infinite nesting). Now Category will not return list of products in JSON response.
    //@JsonManagedReference  // Mark this side as the primary in serialization
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }

}

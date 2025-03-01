package com.ecoapi.goodshopping.repository;

import com.ecoapi.goodshopping.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// provides standard CRUD (Create, Read, Update, Delete) operations for the Image entity
public interface ImageRepository extends JpaRepository<Image, Long> {
    // derive query method that retrieves a list of Image entities based on the productId field
    // Spring Data JPA automatically implements this method based on its name
    // findBy: Indicates that this method is a query method
    // ProductId: Refers to the productId property of the Image entity. Spring Data JPA understand that id is the field of the Product entity associated with the Image
    //Product: Refers to the product field in the Image entity. Id: Refers to the id field of the Product entity.
    List<Image> findByProductId(Long id);

    /* custom query
    @Query("SELECT i FROM Image i WHERE i.productId = :productId")
    List<Image> findByProductId(@Param("productId") Long id);
    */
}

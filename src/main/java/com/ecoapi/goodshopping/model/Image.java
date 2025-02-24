package com.ecoapi.goodshopping.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob; // interface that represents a binary large object stored in a database

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fileName; // stores the name of the image file (e.g., product_image.jpg)
    private String fileType; // stores the MIME type of the file (e.g., image/jpeg)

    @Lob // field should be mapped to a Large Object type in the database
    private Blob image;     // stores binary data of an image file... Binary Large Object
    /*
    @Lob
    private byte[] image; // A byte array can also be used to store binary data. It is simpler but less efficient for very large files.
    Instead of storing the binary data in the database, we can store the file on a file system or cloud storage (e.g., AWS S3) and save only the file path or URL in the database.
     */

    private String downloadUrl; // stores a URL where the image can be downloaded or accessed.

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}

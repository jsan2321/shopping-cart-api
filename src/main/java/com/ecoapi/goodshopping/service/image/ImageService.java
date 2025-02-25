package com.ecoapi.goodshopping.service.image;

import com.ecoapi.goodshopping.dto.ImageDto;
import com.ecoapi.goodshopping.exceptions.ResourceNotFoundException;
import com.ecoapi.goodshopping.model.Image;
import com.ecoapi.goodshopping.model.Product;
import com.ecoapi.goodshopping.repository.ImageRepository;
import com.ecoapi.goodshopping.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                              .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                       .ifPresentOrElse(
                               imageRepository::delete,
                               () -> {
                                   throw new ResourceNotFoundException("No image found with id: " + id);
                               });
    }

    // saves multiple images for a specific product
    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {

        Product product = productService.getProductById(productId);

        // stores the details of the saved images in the form of ImageDto objects
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) { // iterates over the list of MultipartFile objects (uploaded files)
            try {
                // For each file, a new Image entity is created and populated with the following details
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes())); // converts the binary data of the file to a Blob
                image.setProduct(product); // entity to which the image belongs

                String buildDownloadUrl = "/api/v1/images/image/download/"; // base URL
                Image savedImage = imageRepository.save(image);             // saves the image first to generate the ID
                String downloadUrl = buildDownloadUrl+savedImage.getId(); // attaches image's ID
                savedImage.setDownloadUrl(downloadUrl);
                imageRepository.save(savedImage); // no need for a second save if the entity is managed by the persistence context
                /*Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId()); // updates the download URL with the actual id of the saved image
                imageRepository.save(savedImage);*/

                // DTO that stores the details of the saved image
                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch(IOException | SQLException e){ // exceptions to handle file processing or database operation error
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    // updates an existing image with new file data
    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image); // updated Image entity is saved to the database
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

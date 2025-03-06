package com.ecoapi.goodshopping.service.product;

import com.ecoapi.goodshopping.dto.ImageDto;
import com.ecoapi.goodshopping.dto.ProductDto;
import com.ecoapi.goodshopping.exceptions.AlreadyExistsException;
import com.ecoapi.goodshopping.exceptions.ProductNotFoundException;
import com.ecoapi.goodshopping.model.Category;
import com.ecoapi.goodshopping.model.Image;
import com.ecoapi.goodshopping.model.Product;
import com.ecoapi.goodshopping.repository.CategoryRepository;
import com.ecoapi.goodshopping.repository.ImageRepository;
import com.ecoapi.goodshopping.repository.ProductRepository;
import com.ecoapi.goodshopping.request.AddProductRequest;
import com.ecoapi.goodshopping.request.ProductUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Generates a parameterized constructor with the final fields
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {

        if (productExists(request.getName(), request.getBrand())){ // exception thrown with a message set into it
            throw new AlreadyExistsException(request.getBrand() +" "+request.getName()+ " already exists, you may update this product instead!");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                                    .orElseGet(() -> {
                                        Category newCategory = new Category(request.getCategory().getName());
                                        return categoryRepository.save(newCategory);
                                    });

        return productRepository.save(createProduct(request, category));
    }

    private boolean productExists(String name , String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    // helper method to construct a Product object
    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                         .ifPresentOrElse(
                                 productRepository::delete,
                                 () -> { throw new ProductNotFoundException("Product not found!"); }
                         );
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                                .map(productRepository::save)
                                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream()
                       .map(this::convertToDto)
                       .toList(); // converts the transformed object into a List
                       //.collect(Collectors.toList());
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                                         .map(image -> modelMapper.map(image, ImageDto.class))
                                         .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}

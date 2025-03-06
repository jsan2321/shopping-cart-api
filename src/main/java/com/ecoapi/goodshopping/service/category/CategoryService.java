package com.ecoapi.goodshopping.service.category;

import com.ecoapi.goodshopping.exceptions.AlreadyExistsException;
import com.ecoapi.goodshopping.exceptions.ResourceNotFoundException;
import com.ecoapi.goodshopping.model.Category;
import com.ecoapi.goodshopping.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                                 .orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        // if category were null, Optional.of would throw a NullPointerException
        return  Optional.of(category) // Wrap the category in an Optional
                        .filter(c -> !categoryRepository.existsByName(c.getName()))
                        .map(categoryRepository::save)
                        .orElseThrow(() -> new AlreadyExistsException(category.getName()+" already exists"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                       .map(oldCategory -> {
                           oldCategory.setName(category.getName());
                           return categoryRepository.save(oldCategory);
                       })
                       .orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                          .ifPresentOrElse(
                                  categoryRepository::delete,
                                  () -> {throw new ResourceNotFoundException("Category not found!");
                          });
    }
}

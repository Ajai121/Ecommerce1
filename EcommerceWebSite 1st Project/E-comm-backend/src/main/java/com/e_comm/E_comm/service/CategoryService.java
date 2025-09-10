package com.e_comm.E_comm.service;

import com.e_comm.E_comm.model.Category;
import com.e_comm.E_comm.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;


    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public ResponseEntity<?> addCategory(Category category) {
        // Check if category exists (case-insensitive)
        Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(category.getName());

        if (existingCategory.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Category with name '" + category.getName() + "' already exists.");
        }

        // Save new category
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }


    public Category updateCategoryById(Integer id, Category updateCategory) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(updateCategory.getName());
                    existingCategory.setDescription(updateCategory.getDescription());
                    return categoryRepository.save(existingCategory);
                })
                .orElseThrow(()-> new RuntimeException("Category Not found with id : "+id));
    }

    public void deleteCategoryById(Integer id) {
        if (!categoryRepository.existsById(id)){
            throw new RuntimeException("Category with ID " + id + " not found!");
        }
        categoryRepository.deleteById(id);
    }
}

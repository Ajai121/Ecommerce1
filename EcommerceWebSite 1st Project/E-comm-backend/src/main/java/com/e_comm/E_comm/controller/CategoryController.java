package com.e_comm.E_comm.controller;

import com.e_comm.E_comm.model.Category;
import com.e_comm.E_comm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/category")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/allCategory")
    public List<Category> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<Category> updateCategoryById(@PathVariable Integer id, @RequestBody Category updateCategory){
        Category category = categoryService.updateCategoryById(id, updateCategory);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Integer id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("Category is deleted");
    }

}

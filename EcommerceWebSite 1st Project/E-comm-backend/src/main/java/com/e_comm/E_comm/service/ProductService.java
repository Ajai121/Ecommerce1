package com.e_comm.E_comm.service;

import com.e_comm.E_comm.exception.ProductNotFoundException;
import com.e_comm.E_comm.model.Category;
import com.e_comm.E_comm.model.OrderItem;
import com.e_comm.E_comm.model.Product;
import com.e_comm.E_comm.repository.CategoryRepository;
import com.e_comm.E_comm.repository.OrderItemRepository;
import com.e_comm.E_comm.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productRepository.findAllByOrderByProductCodeAsc();
        // Since JPA relationships are already set, category will be included in JSON if Product has @ManyToOne to Category
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    public ResponseEntity<Product> addProduct(Product product) {
        if (product.getCategory() == null || product.getCategory().getName() == null) {
            throw new RuntimeException("Category name must be provided in the product request");
        }

        String categoryName = product.getCategory().getName().trim();

        // Find category by name (ignore case) or create new
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(categoryName);
                    newCategory.setDescription("Auto-created category");
                    return categoryRepository.save(newCategory);
                });

        product.setCategory(category);

        // ✅ Ensure product has an image URL (optional: fallback if none provided)
        if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
            product.setImageUrl("https://via.placeholder.com/300");
            // 👆 default placeholder if client doesn’t send image
        }

        // Generate product code
        List<Product> existingProducts = productRepository.findAllByOrderByProductCodeAsc();
        int counter = 1;
        for (Product p : existingProducts) {
            int number = Integer.parseInt(p.getProductCode().substring(1));
            if (number != counter) {
                product.setProductCode(String.format("P%05d", counter));
                return ResponseEntity.ok(productRepository.save(product));
            }
            counter++;
        }
        product.setProductCode(String.format("P%05d", counter));

        return ResponseEntity.ok(productRepository.save(product));
    }


    public Product getProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode.trim())
                .orElseThrow(() -> new RuntimeException("Product not found with code: " + productCode));
    }

    public List<Product> searchProductByName(String productName) {
        String trimmedName = productName.trim();
        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Product name must not be empty or spaces only.");
        }

        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(trimmedName);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found with name: " + trimmedName);
        }

        return products;
    }

    public List<Product> getProductByCategory(String categoryName) {
        String trimmedName = categoryName.trim();
        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Category name must not be empty or spaces only.");
        }

        List<Product> products = productRepository.findByCategory_NameContainingIgnoreCase(trimmedName);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found in category: " + trimmedName);
        }

        return products;
    }

    @Transactional
    public void deleteProductByCode(String productCode) {
        Product product = productRepository.findByProductCode(productCode.trim())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with code: " + productCode));

        // Before deleting, detach product from order items (set product = null)
        List<OrderItem> orderItems = orderItemRepository.findByProduct(product);
        for (OrderItem item : orderItems) {
            item.setProduct(null);
            orderItemRepository.save(item);
        }

        // Now safely delete product
        productRepository.delete(product);
    }


    public Product updateProductById(Integer id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    // Update category if provided
                    if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getName() != null) {
                        Category category = categoryRepository.findByNameIgnoreCase(updatedProduct.getCategory().getName())
                                .orElseThrow(() -> new RuntimeException("Category not found: " + updatedProduct.getCategory().getName()));
                        existingProduct.setCategory(category);
                    }

                    // Update product fields
                    existingProduct.setProductDetails(updatedProduct.getProductDetails());
                    existingProduct.setProductPrice(updatedProduct.getProductPrice());
                    existingProduct.setProductName(updatedProduct.getProductName());
                    existingProduct.setProductQuantity(updatedProduct.getProductQuantity());

                    // ✅ Update image URL (if provided)
                    if (updatedProduct.getImageUrl() != null && !updatedProduct.getImageUrl().isBlank()) {
                        existingProduct.setImageUrl(updatedProduct.getImageUrl());
                    }

                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

}

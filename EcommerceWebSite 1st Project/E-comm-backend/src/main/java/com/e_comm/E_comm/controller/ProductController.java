package com.e_comm.E_comm.controller;

import com.e_comm.E_comm.model.Product;
import com.e_comm.E_comm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/allProduct")
    public ResponseEntity<List<Product>> getAllProduct(){
        return productService.getAllProduct();
    }

    @PostMapping(value = "/addProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @GetMapping("/code/{productCode}")
    public ResponseEntity<Product> getProductByCode(@PathVariable String productCode){
        return ResponseEntity.ok(productService.getProductByCode(productCode));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductByName(@RequestParam String productName){
        return ResponseEntity.ok(productService.searchProductByName(productName));
    }

    @GetMapping("/category/{productCategory}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable String productCategory){
        return ResponseEntity.ok(productService.getProductByCategory(productCategory));
    }

    @DeleteMapping("/{productCode}")
    public ResponseEntity<String> deleteProductByCode(@PathVariable String productCode){
        productService.deleteProductByCode(productCode);
        return ResponseEntity.ok("Product is Deleted Successfully");
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Product> updateProductById(@PathVariable Integer id, @RequestBody Product updatedProduct){
        Product product = productService.updateProductById(id, updatedProduct);
        return ResponseEntity.ok(product);
    }

}

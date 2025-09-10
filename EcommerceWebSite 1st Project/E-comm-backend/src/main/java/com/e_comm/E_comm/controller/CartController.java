package com.e_comm.E_comm.controller;

import com.e_comm.E_comm.model.Cart;
import com.e_comm.E_comm.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<Cart> addProduct(@PathVariable Long userId,
                                           @PathVariable Long productId,
                                           @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addProductToCart(userId, productId, quantity));
    }

    @PutMapping("/{userId}/update/{productId}")
    public ResponseEntity<Cart> updateQuantity(@PathVariable Long userId,
                                               @PathVariable Long productId,
                                               @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(userId, productId, quantity));
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long userId,
                                              @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<BigDecimal> getTotal(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartTotal(userId));
    }
}

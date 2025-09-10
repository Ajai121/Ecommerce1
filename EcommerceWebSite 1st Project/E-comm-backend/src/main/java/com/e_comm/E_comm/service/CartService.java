package com.e_comm.E_comm.service;

import com.e_comm.E_comm.model.*;
import com.e_comm.E_comm.repository.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Cart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    public Cart addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId.intValue())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartItemRepository.findByCartAndProduct(cart, product).ifPresentOrElse(item -> {
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        }, () -> {
            CartItem newItem = new CartItem(product, quantity, product.getProductPrice(), cart);
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        });

        return cartRepository.save(cart);
    }

    public Cart updateQuantity(Long userId, Long productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId.intValue())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return cart;
    }

    public void removeProductFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId.intValue())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartItemRepository.findByCartAndProduct(cart, product)
                .ifPresent(cartItemRepository::delete);
    }

    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public BigDecimal getCartTotal(Long userId) {
        Cart cart = getCartByUserId(userId);
        return cart.getCartItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

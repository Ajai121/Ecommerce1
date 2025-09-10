package com.e_comm.E_comm.service;

import com.e_comm.E_comm.model.*;
import com.e_comm.E_comm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;



    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        CartService cartService,
                        UserRepository userRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order placeOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartService.getCartByUserId(userId);
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            if (product.getProductQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getProductName());
            }

            // Reduce stock
            product.setProductQuantity(product.getProductQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            // Create order item with snapshot
            BigDecimal itemTotal = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            OrderItem orderItem = new OrderItem(product, cartItem.getQuantity(), itemTotal, order);

            order.getOrderItems().add(orderItem);
            totalAmount = totalAmount.add(itemTotal);
        }

        order.setTotalAmount(totalAmount);

        // Save order & order items in one go (thanks to cascade)
        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cartService.clearCart(userId);

        return savedOrder;
    }

    public List<Order> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // prevent cancelling shipped or delivered orders
        if (order.getOrderStatus() == OrderStatus.SHIPPED ||
                order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel shipped or delivered order");
        }

        // Restore product stock for each order item
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            if (product != null) { // safeguard in case product was deleted later
                product.setProductQuantity(product.getProductQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        // Mark order as cancelled
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Transactional
    public boolean deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            return false;
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Restore stock if not already cancelled
        if (order.getOrderStatus() != OrderStatus.CANCELLED) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                if (product != null) {
                    product.setProductQuantity(product.getProductQuantity() + item.getQuantity());
                    productRepository.save(product);
                }
            }
        }

        // First delete order items (if cascade not enabled)
        orderItemRepository.deleteAll(order.getOrderItems());

        // Then delete the order itself
        orderRepository.delete(order);

        return true;
    }

}

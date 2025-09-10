package com.e_comm.E_comm.service;

import com.e_comm.E_comm.model.Order;
import com.e_comm.E_comm.model.OrderItem;
import com.e_comm.E_comm.model.Product;
import com.e_comm.E_comm.model.User;
import com.e_comm.E_comm.repository.OrderRepository;
import com.e_comm.E_comm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AdminService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        List<Order> allOrders = orderRepository.findAll();

        // Basic stats
        stats.put("totalUsers", userRepository.count());
        stats.put("totalOrders", allOrders.size());
        BigDecimal totalSales = allOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalSales", totalSales);

        // Top selling products
        Map<Product, Integer> productSalesMap = new HashMap<>();
        for (Order order : allOrders) {
            for (OrderItem item : order.getOrderItems()) {
                productSalesMap.put(item.getProduct(),
                        productSalesMap.getOrDefault(item.getProduct(), 0) + item.getQuantity());
            }
        }

        List<Map<String, Object>> topProducts = productSalesMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // descending
                .limit(5)
                .map(e -> {
                    Map<String, Object> p = new HashMap<>();
                    p.put("productName", e.getKey().getProductName());
                    p.put("quantitySold", e.getValue());
                    return p;
                })
                .collect(Collectors.toList());
        stats.put("topProducts", topProducts);

        // Recent orders (last 5)
        List<Map<String, Object>> recentOrders = allOrders.stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .limit(5)
                .map(o -> {
                    Map<String, Object> ro = new HashMap<>();
                    ro.put("orderId", o.getId());
                    ro.put("user", o.getUser().getUsername());
                    ro.put("totalAmount", o.getTotalAmount());
                    ro.put("status", o.getOrderStatus());
                    ro.put("createdAt", o.getCreatedAt());
                    return ro;
                })
                .collect(Collectors.toList());
        stats.put("recentOrders", recentOrders);

        return stats;
    }
}

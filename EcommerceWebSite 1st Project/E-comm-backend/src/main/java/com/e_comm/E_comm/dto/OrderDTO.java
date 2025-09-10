package com.e_comm.E_comm.dto;

import com.e_comm.E_comm.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private double totalAmount;
    private String shippingAddress;
    private OrderStatus orderStatus;
    private List<OrderItemDTO> items;
}

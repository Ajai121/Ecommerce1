package com.e_comm.E_comm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    private String productName;
    private int quantity;
    private double price;
}

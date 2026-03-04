package com.inventory.dto;
import com.inventory.model.Order;
import lombok.Data;

@Data public class OrderStatusUpdateDTO {
    private Order.OrderStatus status;
}

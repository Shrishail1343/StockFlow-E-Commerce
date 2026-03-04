// OrderCreateDTO.java
package com.inventory.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateDTO {
    private Long customerId;
    private List<OrderItemCreateDTO> items;
    private String shippingAddress;
    private BigDecimal shippingCost;
    private String notes;
}

package com.inventory.dto;
import lombok.Data;

@Data public class StockUpdateDTO {
    private Integer quantity;
    private String changeType = "ADJUSTMENT";
}

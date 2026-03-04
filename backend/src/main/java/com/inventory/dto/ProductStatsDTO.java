package com.inventory.dto;
import lombok.*;
import java.math.BigDecimal;

@Data @AllArgsConstructor @NoArgsConstructor
public class ProductStatsDTO {
    private long total;
    private long lowStock;
    private long active;
}

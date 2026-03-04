package com.inventory.dto;
import lombok.*;
import java.math.BigDecimal;

@Data @AllArgsConstructor @NoArgsConstructor
public class OrderStatsDTO {
    private long total;
    private long pending;
    private long processing;
    private long shipped;
    private long delivered;
    private BigDecimal totalRevenue;
    private BigDecimal monthRevenue;
}

package com.inventory.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardStatsDTO {
    private long totalProducts;
    private long lowStockCount;
    private long totalOrders;
    private long pendingOrders;
    private BigDecimal totalRevenue;
    private BigDecimal monthRevenue;
}

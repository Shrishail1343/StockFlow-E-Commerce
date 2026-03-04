package com.inventory.service;

import com.inventory.dto.DashboardStatsDTO;
import com.inventory.model.Order;
import com.inventory.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO dto = new DashboardStatsDTO();
        dto.setTotalProducts(productRepository.count());
        dto.setLowStockCount(productRepository.countLowStockProducts());
        dto.setTotalOrders(orderRepository.count());
        dto.setPendingOrders(orderRepository.countByStatus(Order.OrderStatus.PENDING));
        BigDecimal rev = orderRepository.getTotalRevenue();
        dto.setTotalRevenue(rev != null ? rev : BigDecimal.ZERO);
        BigDecimal monthRev = orderRepository.getMonthRevenue();
        dto.setMonthRevenue(monthRev != null ? monthRev : BigDecimal.ZERO);
        return dto;
    }

    public List<?> getRecentOrders(int limit) {
        return orderRepository.findTopRecentOrders(PageRequest.of(0, limit));
    }

    public List<?> getTopSellingProducts(int limit) {
        return productRepository.findAll(PageRequest.of(0, limit)).getContent();
    }
}

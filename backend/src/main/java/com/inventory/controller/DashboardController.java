package com.inventory.controller;

import com.inventory.dto.DashboardStatsDTO;
import com.inventory.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping("/recent-orders")
    public ResponseEntity<?> getRecentOrders() {
        return ResponseEntity.ok(dashboardService.getRecentOrders(5));
    }

    @GetMapping("/top-products")
    public ResponseEntity<?> getTopProducts() {
        return ResponseEntity.ok(dashboardService.getTopSellingProducts(5));
    }
}

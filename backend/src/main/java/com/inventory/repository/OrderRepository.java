package com.inventory.repository;

import com.inventory.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o LEFT JOIN o.customer c WHERE " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:search IS NULL OR o.orderNumber LIKE CONCAT('%', :search, '%') OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Order> findWithFilters(@Param("status") String status,
                                 @Param("startDate") String startDate,
                                 @Param("endDate") String endDate,
                                 @Param("search") String search,
                                 Pageable pageable);

    long countByStatus(Order.OrderStatus status);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED'")
    BigDecimal getTotalRevenue();

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED' AND MONTH(o.createdAt) = MONTH(CURRENT_DATE) AND YEAR(o.createdAt) = YEAR(CURRENT_DATE)")
    BigDecimal getMonthRevenue();

    @Query(value = "SELECT MONTH(created_at) as month, YEAR(created_at) as year, SUM(total_amount) as revenue, COUNT(*) as orders FROM orders WHERE status = 'DELIVERED' AND created_at >= DATE_SUB(NOW(), INTERVAL 12 MONTH) GROUP BY YEAR(created_at), MONTH(created_at) ORDER BY year, month", nativeQuery = true)
    List<Map<String, Object>> getMonthlyRevenueData();

    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findTopRecentOrders(Pageable pageable);
}

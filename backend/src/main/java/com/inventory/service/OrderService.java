package com.inventory.service;

import com.inventory.dto.*;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.*;
import com.inventory.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public Page<OrderDTO> getAllOrders(PageRequest pageRequest, String status,
                                       String startDate, String endDate, String search) {
        return orderRepository.findWithFilters(status, startDate, endDate, search, pageRequest)
                .map(this::toDTO);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return toDTO(order);
    }

    public OrderDTO createOrder(OrderCreateDTO dto) {
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
            order.setCustomer(customer);
        }

        order.setShippingAddress(dto.getShippingAddress());
        order.setNotes(dto.getNotes());
        order.setStatus(Order.OrderStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItemCreateDTO itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemDto.getProductId()));

            // Decrement stock
            productService.decrementStock(itemDto.getProductId(), itemDto.getQuantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
            items.add(item);
            subtotal = subtotal.add(item.getSubtotal());
        }

        order.setOrderItems(items);
        order.setSubtotal(subtotal);
        order.setTax(subtotal.multiply(BigDecimal.valueOf(0.08))); // 8% tax
        order.setShippingCost(dto.getShippingCost() != null ? dto.getShippingCost() : BigDecimal.valueOf(9.99));
        order.setTotalAmount(order.getSubtotal().add(order.getTax()).add(order.getShippingCost()));

        Order saved = orderRepository.save(order);
        return toDTO(saved);
    }

    public OrderDTO updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        // If cancelling, restore stock
        if (status == Order.OrderStatus.CANCELLED && order.getStatus() != Order.OrderStatus.CANCELLED) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(status);
        Order saved = orderRepository.save(order);
        return toDTO(saved);
    }

    public void cancelOrder(Long id) {
        updateOrderStatus(id, Order.OrderStatus.CANCELLED);
    }

    public OrderStatsDTO getOrderStats() {
        long total = orderRepository.count();
        long pending = orderRepository.countByStatus(Order.OrderStatus.PENDING);
        long processing = orderRepository.countByStatus(Order.OrderStatus.PROCESSING);
        long shipped = orderRepository.countByStatus(Order.OrderStatus.SHIPPED);
        long delivered = orderRepository.countByStatus(Order.OrderStatus.DELIVERED);
        BigDecimal revenue = orderRepository.getTotalRevenue();
        BigDecimal monthRevenue = orderRepository.getMonthRevenue();
        return new OrderStatsDTO(total, pending, processing, shipped, delivered,
                revenue != null ? revenue : BigDecimal.ZERO,
                monthRevenue != null ? monthRevenue : BigDecimal.ZERO);
    }

    public List<Map<String, Object>> getMonthlyRevenue() {
        return orderRepository.getMonthlyRevenueData();
    }

    private OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setSubtotal(order.getSubtotal());
        dto.setTax(order.getTax());
        dto.setShippingCost(order.getShippingCost());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        if (order.getCustomer() != null) {
            dto.setCustomerName(order.getCustomer().getName());
            dto.setCustomerEmail(order.getCustomer().getEmail());
            dto.setCustomerId(order.getCustomer().getId());
        }

        if (order.getOrderItems() != null) {
            dto.setItems(order.getOrderItems().stream().map(item -> {
                OrderItemDTO itemDto = new OrderItemDTO();
                itemDto.setId(item.getId());
                itemDto.setProductId(item.getProduct().getId());
                itemDto.setProductName(item.getProductName());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setUnitPrice(item.getUnitPrice());
                itemDto.setSubtotal(item.getSubtotal());
                return itemDto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }

    private String generateOrderNumber() {
        return "ORD-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%04d", (int)(Math.random() * 10000));
    }
}

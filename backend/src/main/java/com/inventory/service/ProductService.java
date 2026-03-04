package com.inventory.service;

import com.inventory.dto.*;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.Category;
import com.inventory.model.Product;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryLogService inventoryLogService;
    private final ModelMapper modelMapper;

    public Page<ProductDTO> getAllProducts(PageRequest pageRequest, String search,
                                          String category, String status) {
        return productRepository.findWithFilters(search, category, status, pageRequest)
                .map(this::toDTO);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return toDTO(product);
    }

    public ProductDTO createProduct(ProductCreateDTO dto) {
        Product product = new Product();
        mapDtoToProduct(dto, product);
        product.setSku(generateSku(dto.getName()));
        Product saved = productRepository.save(product);
        inventoryLogService.logInventoryChange(saved, "RESTOCK", dto.getStockQuantity(), 0, null);
        return toDTO(saved);
    }

    public ProductDTO updateProduct(Long id, ProductCreateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        int oldQty = product.getStockQuantity();
        mapDtoToProduct(dto, product);
        Product saved = productRepository.save(product);
        if (dto.getStockQuantity() != null && !dto.getStockQuantity().equals(oldQty)) {
            int diff = dto.getStockQuantity() - oldQty;
            inventoryLogService.logInventoryChange(saved, diff > 0 ? "RESTOCK" : "ADJUSTMENT",
                    Math.abs(diff), oldQty, null);
        }
        return toDTO(saved);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    public List<ProductDTO> getLowStockProducts() {
        return productRepository.findLowStockProducts()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProductDTO updateStock(Long id, StockUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        int oldQty = product.getStockQuantity();
        product.setStockQuantity(dto.getQuantity());
        Product saved = productRepository.save(product);
        inventoryLogService.logInventoryChange(saved, dto.getChangeType(),
                Math.abs(dto.getQuantity() - oldQty), oldQty, null);
        return toDTO(saved);
    }

    public ProductStatsDTO getProductStats() {
        long total = productRepository.count();
        long lowStock = productRepository.countLowStockProducts();
        long active = productRepository.countByStatus(Product.ProductStatus.ACTIVE);
        return new ProductStatsDTO(total, lowStock, active);
    }

    // Internal method to decrement stock on order
    public void decrementStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }
        int oldQty = product.getStockQuantity();
        product.setStockQuantity(oldQty - quantity);
        Product saved = productRepository.save(product);
        inventoryLogService.logInventoryChange(saved, "SALE", quantity, oldQty, null);
    }

    private void mapDtoToProduct(ProductCreateDTO dto, Product product) {
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCostPrice(dto.getCostPrice());
        product.setStockQuantity(dto.getStockQuantity() != null ? dto.getStockQuantity() : 0);
        product.setLowStockThreshold(dto.getLowStockThreshold() != null ? dto.getLowStockThreshold() : 10);
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : Product.ProductStatus.ACTIVE);
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        if (dto.getCategoryId() != null) {
            Category cat = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(cat);
        }
    }

    private String generateSku(String name) {
        String prefix = name.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        prefix = prefix.substring(0, Math.min(4, prefix.length()));
        return prefix + "-" + System.currentTimeMillis() % 10000;
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = modelMapper.map(product, ProductDTO.class);
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
            dto.setCategoryId(product.getCategory().getId());
        }
        dto.setLowStock(product.isLowStock());
        return dto;
    }
}

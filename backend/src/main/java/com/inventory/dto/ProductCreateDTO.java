package com.inventory.dto;
import com.inventory.model.Product;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductCreateDTO {
    @NotBlank private String name;
    private Long categoryId;
    @NotNull @DecimalMin("0.01") private BigDecimal price;
    private BigDecimal costPrice;
    private Integer stockQuantity;
    private Integer lowStockThreshold;
    private Product.ProductStatus status;
    private String description;
    private String imageUrl;
}

package com.inventory.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

// OrderItemDTO
@Data class OrderItemDTO_temp {}

// Use these as separate files but provided inline for reference

/* OrderItemDTO.java */
// package com.inventory.dto;
// @Data public class OrderItemDTO { Long id; Long productId; String productName; Integer quantity; BigDecimal unitPrice; BigDecimal subtotal; }

/* OrderItemCreateDTO.java */
// package com.inventory.dto;
// @Data public class OrderItemCreateDTO { Long productId; Integer quantity; }

/* OrderStatusUpdateDTO.java */
// package com.inventory.dto;
// import com.inventory.model.Order;
// @Data public class OrderStatusUpdateDTO { Order.OrderStatus status; }

/* StockUpdateDTO.java */
// package com.inventory.dto;
// @Data public class StockUpdateDTO { Integer quantity; String changeType; }

/* LoginRequest.java */
// package com.inventory.dto;
// @Data public class LoginRequest { @NotBlank String username; @NotBlank String password; }

/* AuthResponse.java */
// package com.inventory.dto;
// @Data @AllArgsConstructor public class AuthResponse { String token; String tokenType; UserDTO user; }

/* ApiResponse.java */
// package com.inventory.dto;
// @Data @AllArgsConstructor public class ApiResponse { boolean success; String message; }

package com.inventory.dto;
import lombok.*;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.util.List;

// AuthResponse
@Data @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType;
    private UserDTO user;
}

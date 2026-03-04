// UserDTO.java
package com.inventory.dto;
import lombok.Data;
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String role;
}

// ===========================

// LoginRequest.java
// package com.inventory.dto;
// import jakarta.validation.constraints.NotBlank;
// import lombok.Data;
// @Data public class LoginRequest { @NotBlank String username; @NotBlank String password; }

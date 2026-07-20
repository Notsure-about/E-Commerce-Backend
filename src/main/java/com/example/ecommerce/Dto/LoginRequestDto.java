package com.example.ecommerce.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
     @NotBlank(message = "email is required")
     @Email(message = "Email must be Valid")
     @Size(max = 120)
     private String email;
     @NotBlank(message = "password is required")
    @Size(min = 5,max = 20)
    private String password;
}

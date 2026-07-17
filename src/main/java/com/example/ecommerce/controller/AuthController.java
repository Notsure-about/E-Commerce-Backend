package com.example.ecommerce.controller;

import com.example.ecommerce.Dto.LoginRequestDto;
import com.example.ecommerce.Dto.LoginResponseDto;
import com.example.ecommerce.Dto.RegisterRequestDto;
import com.example.ecommerce.Dto.UserDto;
import com.example.ecommerce.Exception.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }
    @PostMapping("/register")
   public  ResponseEntity<UserDto> register(@RequestBody RegisterRequestDto dto){
        return  ResponseEntity.ok(authService.register(dto));
    }
}

package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.RegisterRequestDto;
import com.example.ecommerce.Dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto CreateUser(RegisterRequestDto dto);
    UserDto UpdateUser(Long id , UserDto dto);
    UserDto GetUserById(Long id);
    List<UserDto> GetAllUsers();
    void  DeleteUserById(Long id);
}

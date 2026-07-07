package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.RegisterRequestDto;
import com.example.ecommerce.Dto.UserDto;
import com.example.ecommerce.Entity.User;
import com.example.ecommerce.Exception.ResourceNotFoundException;
import com.example.ecommerce.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements  UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    public UserDto ConvertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
    @Override
    public UserDto CreateUser(RegisterRequestDto dto) {
      User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // later: encode password
        user.setAbout(dto.getAbout());
          User saved = userRepository.save(user);
        return ConvertToDto(saved);
    }

    @Override
    public UserDto UpdateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User","userId",id));
        user.setAbout(dto.getAbout());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        User saved = userRepository.save(user);
        return ConvertToDto(saved);
    }

    @Override
    public UserDto GetUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User","userId",id));
        return ConvertToDto(user);
    }

    @Override
    public List<UserDto> GetAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::ConvertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void DeleteUserById(Long id) {
   User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User","userId",id));
   userRepository.delete(user);
    }
}


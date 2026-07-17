package com.example.ecommerce.Exception;

import com.example.ecommerce.Dto.LoginRequestDto;
import com.example.ecommerce.Dto.LoginResponseDto;
import com.example.ecommerce.Dto.RegisterRequestDto;
import com.example.ecommerce.Dto.UserDto;
import com.example.ecommerce.Entity.User;
import com.example.ecommerce.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
 @Autowired
    private UserRepository userRepository;
 @Autowired
    private PasswordEncoder passwordEncoder;
 @Autowired
 private ModelMapper modelMapper;
 public UserDto register(RegisterRequestDto dto){
   if(userRepository.findByEmail(dto.getEmail()).isPresent()){
       throw new InvalidRequestException("email is already registered");

   }
   User user  = new User();
   user.setName(dto.getName());
   user.setEmail(dto.getEmail());
   user.setPassword(passwordEncoder.encode(dto.getPassword()));
   user.setAbout(dto.getAbout());
    User saveduser =   userRepository.save(user);
    return  modelMapper.map(saveduser , UserDto.class);
 }
   public LoginResponseDto login (LoginRequestDto dto){

           // Spring handles email + password verification
           Authentication authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           dto.getEmail(), dto.getPassword()));

           // get user from authentication
           User user = (User) authentication.getPrincipal();

           // generate token
           String token = jwtService.GenerateToken(user.getEmail());

           // return response
           return new LoginResponseDto(token, user.getEmail());
       }

 }


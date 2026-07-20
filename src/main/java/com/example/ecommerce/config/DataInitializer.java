package com.example.ecommerce.config;

import com.example.ecommerce.Entity.Role;
import com.example.ecommerce.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository repository;
    @Override
    public void run(String... args) throws Exception {
    if(repository.count()==0){
//        Role role = new Role();
        repository.save(new Role("ROLE_ADMIN"));
        repository.save(new Role("ROLE_USER"));
        System.out.println("Roles Are Initialized");
    }
    }
}

package com.example.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
      @Column
    private String title;
    @Column
    private  String description;
    @Column
    private double price;
    @Column
     private  int stock;
    @Column
    private String imageName;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}

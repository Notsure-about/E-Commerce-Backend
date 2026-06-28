package com.example.ecommerce.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private double price;
    private int stock;
    private  String imageName;

    private Long categoryId;
    private String categoryTitle;
}

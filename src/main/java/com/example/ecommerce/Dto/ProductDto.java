package com.example.ecommerce.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductDto {
    private Long id;
    @NotBlank(message = "must fill the name of product")
    private String title;
    private String description;
    @NotBlank(message = "price should not me epmty")
    private double price;
    @NotBlank(message = "enter the stock ")
    private int stock;
    private  String imageName;

    private Long categoryId;
    private String categoryTitle;
}

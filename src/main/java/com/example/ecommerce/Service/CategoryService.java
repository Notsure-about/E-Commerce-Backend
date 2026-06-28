package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.CategoryDto;

import java.util.List;


public interface CategoryService {
CategoryDto CreateCategory(CategoryDto dto);
CategoryDto updateCategory(Long id ,CategoryDto dto);
CategoryDto GetCategoryById(Long id);
List<CategoryDto>getAllCategories();
void deleteCategoryById(Long id);


}

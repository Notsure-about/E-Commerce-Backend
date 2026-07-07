package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.CategoryDto;
import com.example.ecommerce.Entity.Category;
import com.example.ecommerce.Exception.ResourceNotFoundException;
import com.example.ecommerce.Repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements  CategoryService{
@Autowired
private CategoryRepository categoryRepository;
@Autowired
private ModelMapper modelMapper;
public CategoryDto ConvertToDto(Category category){
    return modelMapper.map(category, CategoryDto.class);
}
public Category ConvertToEnt(CategoryDto dto){
    return  modelMapper.map(dto, Category.class);
}

    @Override
    public CategoryDto CreateCategory(CategoryDto dto) {
     Category category = ConvertToEnt(dto);
     Category saved = categoryRepository.save(category);
        return ConvertToDto(saved) ;
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
      Category category =  categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",id));
         modelMapper.map(dto, category);
         Category update = categoryRepository.save(category);
         return ConvertToDto(update);
    }

    @Override
    public CategoryDto GetCategoryById(Long id) {
    Category category  = categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",id));
        return ConvertToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::ConvertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategoryById(Long id) {
    Category category =  categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",id));
       categoryRepository.delete(category);
    }
}

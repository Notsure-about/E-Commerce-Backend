package com.example.ecommerce.Service;

import com.example.ecommerce.Dto.ProductDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductDto createProduct(ProductDto dto);
    ProductDto UpdateProduct(Long id, ProductDto dto);
    void DeleteProduct(Long id);
    ProductDto GetProductById(Long id);
    Page<ProductDto> getAllProducts(int page, int size, String sortBy, String keyword);
}

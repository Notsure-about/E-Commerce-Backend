package com.example.ecommerce.Service;

import com.example.ecommerce.Entity.Category;
import com.example.ecommerce.Entity.Product;
import com.example.ecommerce.Exception.ResourceNotFoundException;
import com.example.ecommerce.Repository.CategoryRepository;
import com.example.ecommerce.Repository.ProductRepository;
import com.example.ecommerce.Dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService{
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private  final CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    private Product convertToProduct(ProductDto dto){return modelMapper.map(dto, Product.class);}
   private ProductDto convertToDto(Product product){
       ProductDto dto = modelMapper.map(product, ProductDto.class);
       // Product entity has a Category relationship, while ProductDto stores flattened category fields.
       if (product.getCategory() != null) {
           dto.setCategoryId(product.getCategory().getId());
           dto.setCategoryTitle(product.getCategory().getTitle());
       }
       return dto;
   }
    public ProductDto createProduct(ProductDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",dto.getCategoryId()));
        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setStock(dto.getStock());
        product.setPrice(dto.getPrice());
        product.setImageName(dto.getImageName());
        product.setCategory(category);
        Product saved = productRepository.save(product);
        return  convertToDto(saved);
    }
    @Override
    public ProductDto UpdateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product","productId",id));
        product.setStock(dto.getStock());
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setImageName(dto.getImageName());
        if (dto.getCategoryId()!=null){
            Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",dto.getCategoryId()));
            product.setCategory(category);
        }
        Product updated = productRepository.save(product);
        return convertToDto(updated);
    }

    @Override
    public void DeleteProduct(Long id) {
          Product product = productRepository.findById(id).orElseThrow(()->new  ResourceNotFoundException("Product","productId",id));
         productRepository.delete(product);
    }

    @Override
    public ProductDto GetProductById(Long id) {
    Product product = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product","productId",id));
        return convertToDto(product);
    }

    @Override
    public Page<ProductDto> getAllProducts(int page, int size, String sortBy, String keyword) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy));
        Page<Product>products;
        if(keyword!=null && !keyword.isEmpty())
        {
      products = productRepository.findByTitleContaining(keyword,pageable);
        }
        else{
            products= productRepository.findAll(pageable);
        }
        return products.map(this::convertToDto);
    }
}

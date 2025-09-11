package com.example.productInventoryManagementService.service;

import com.example.productInventoryManagementService.entity.Category;
import com.example.productInventoryManagementService.entity.Product;
import com.example.productInventoryManagementService.entity.dto.request.CategoryCreateRequest;
import com.example.productInventoryManagementService.entity.dto.request.CategoryUpdateRequest;
import com.example.productInventoryManagementService.entity.dto.response.CategoryResponse;
import com.example.productInventoryManagementService.repository.CategoryRepository;
import com.example.productInventoryManagementService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public final ProductRepository productRepository;

   public List<CategoryResponse> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(this::mapToCategoryResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {
       Category category = categoryRepository.findById(id)
               .orElseThrow(() -> new IllegalArgumentException("Id not found"));
       return  mapToCategoryResponse(category);
    }

    public CategoryResponse create(CategoryCreateRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        categoryRepository.save(category);

        return  mapToCategoryResponse(category);
    }

    public CategoryResponse update(Long id, CategoryUpdateRequest request) {
       Category category = categoryRepository.findById(id)
               .orElseThrow(() -> new IllegalArgumentException("Id not found"));
       category.setName(request.getName());
       category.setDescription(request.getDescription());

       categoryRepository.save(category);

       return  mapToCategoryResponse(category);
    }

    public void  delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id not found"));
         productRepository.findByCategory(category)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
         categoryRepository.delete(category);

    }


    CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .build();
    }
}

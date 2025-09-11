package com.example.productInventoryManagementService.service;

import com.example.productInventoryManagementService.entity.Category;
import com.example.productInventoryManagementService.entity.Product;
import com.example.productInventoryManagementService.entity.dto.request.CategoryCreateRequest;
import com.example.productInventoryManagementService.entity.dto.request.CategoryUpdateRequest;
import com.example.productInventoryManagementService.entity.dto.response.CategoryResponse;
import com.example.productInventoryManagementService.repository.CategoryRepository;
import com.example.productInventoryManagementService.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private  CategoryService categoryService;

    @Test
    void shouldSuccessWhenGetAllListCategory() {
        List<Category> categoryList = new ArrayList<>();
        Category category1 = Category.builder()
                .id(1L)
                .name("food")
                .description("food for people")
                .build();

        Category category2 = Category.builder()
                .id(1L)
                .name("food")
                .description("food for people")
                .build();

        categoryList.add(category1);
        categoryList.add(category2);


        when(categoryRepository.findAll()).thenReturn(categoryList);

        List<CategoryResponse> categoryResponses = categoryService.getAllCategory();

        assertEquals(2, categoryResponses.size());
        assertEquals("food", categoryResponses.getFirst().getName());
        assertEquals("food for people", categoryResponses.getFirst().getDescription());
    }

    @Test
    void shouldCreateCategorySuccess() {
        CategoryCreateRequest request = CategoryCreateRequest.builder()
                .name("material")
                .description("material for building")
                .build();

        Category category1 = Category.builder()
                .id(1L)
                .name("food")
                .description("food for people")
                .build();
        when(categoryRepository.save(any())).thenReturn(category1);

        CategoryResponse categoryResponse = categoryService.create(request);

        assertEquals("material", categoryResponse.getName());
        assertEquals("material for building", categoryResponse.getDescription());
    }

    @Test
    void shouldSuccessWhenCallGetCategoryById() {
        Category category = Category.builder()
                .id(1L)
                .name("food")
                .description("food for people")
                .build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));

        CategoryResponse categoryResponse = categoryService.getCategoryById(1L);

        assertEquals("food", categoryResponse.getName());
        assertEquals("food for people", categoryResponse.getDescription());
    }

    @Test
    void throwWhenIdNotFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void shouldUpdateCategorySuccess() {
        CategoryUpdateRequest request = CategoryUpdateRequest.builder()
                .name("material")
                .description("material for building")
                .build();

        Category category1 = Category.builder()
                .id(1L)
                .name("food")
                .description("food for people")
                .build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category1));
        when(categoryRepository.save(any())).thenReturn(category1);

        CategoryResponse categoryResponse = categoryService.update(1L, request);

        assertEquals("material", categoryResponse.getName());
        assertEquals("material for building", categoryResponse.getDescription());
    }

    @Test
    void shouldDeleteSuccess() {
        Long id = 1L;
        Category category1 = Category.builder()
                .id(1L)
                .name("food")
                .description("food for people")
                .build();
        Product product = Product.builder()
                .sku("test")
                .name("food")
                .description("")
                .category(category1)
                .build();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category1));
        when(productRepository.findByCategory(category1)).thenReturn(Optional.of(product));

        categoryService.delete(id);

        verify(categoryRepository).findById(id);
        verify(productRepository).findByCategory(category1);
        verify(categoryRepository).delete(category1);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundOnDelete() {
        Long id = 1L;
        
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.delete(id));
        
        verify(categoryRepository).findById(id);
        verify(productRepository, never()).findByCategory(any());
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnDelete() {
        Long id = 1L;
        Category category1 = Category.builder()
                .id(1L)
                .name("food")
                .description("food for people")
                .build();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category1));
        when(productRepository.findByCategory(category1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.delete(id));
        
        verify(categoryRepository).findById(id);
        verify(productRepository).findByCategory(category1);
        verify(categoryRepository, never()).delete(any());
    }


}
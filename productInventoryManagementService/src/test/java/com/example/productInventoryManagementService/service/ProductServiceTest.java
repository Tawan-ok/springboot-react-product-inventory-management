package com.example.productInventoryManagementService.service;

import com.example.productInventoryManagementService.entity.Category;
import com.example.productInventoryManagementService.entity.Product;
import com.example.productInventoryManagementService.entity.dto.request.ProductCreateRequest;
import com.example.productInventoryManagementService.entity.dto.request.ProductUpdateRequest;
import com.example.productInventoryManagementService.entity.dto.response.ProductResponse;
import com.example.productInventoryManagementService.repository.CategoryRepository;
import com.example.productInventoryManagementService.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    List<Product> productsList;

    @BeforeEach
    void setUp() {
        productsList = new ArrayList<>();
        Category category = Category.builder()
                .id(1L)
                .name("food")
                .build();
        Product product1 = Product.builder()
                .id(1L)
                .sku("SKU001")
                .name("Test Product 1")
                .price(BigDecimal.valueOf(99.99))
                .quantity(10)
                .category(category)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .sku("SKU002")
                .name("Test Product 2")
                .price(BigDecimal.valueOf(149.99))
                .quantity(5)
                .category(category)
                .build();

        productsList.add(product1);
        productsList.add(product2);
    }

    @Test
    void shouldSuccessWhenGetAllProduct() {
        when(productRepository.findAll()).thenReturn(productsList);

        List<ProductResponse> productResponses = productService.getAllList();

        assertEquals(2, productResponses.size());
        assertEquals(2, productResponses.get(1).getId());
        assertEquals("SKU002", productResponses.get(1).getSku());
        assertEquals("Test Product 2", productResponses.get(1).getName());
        assertEquals(10, productResponses.get(0).getQuantity());
        assertEquals("food", productResponses.get(0).getCategory().getName());
        assertEquals("food", productResponses.get(1).getCategory().getName());
    }

    @Test
    void  shouldSuccessWhenCreateProduct() {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sku("SKU001")
                .name("Test Product 1")
                .price(BigDecimal.valueOf(99.99))
                .quantity(10)
                .category("food")
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("food")
                .build();

        Product product = Product.builder()
                .id(1L)
                .sku("SKU001")
                .name("Test Product 1")
                .price(BigDecimal.valueOf(99.99))
                .quantity(10)
                .category(category)
                .build();
        when(categoryRepository.findByName(any())).thenReturn(Optional.ofNullable(category));
        when(productRepository.save(any())).thenReturn(product);

        ProductResponse productResponse = productService.create(request);

        assertEquals("SKU001", productResponse.getSku());
        assertEquals("food", productResponse.getCategory().getName());
        assertEquals(10, productResponse.getQuantity());

    }

    @Test
    void shouldSuccessWhenUpdateProduct() {

        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .sku("SKU002")
                .name("Test Product 1")
                .price(BigDecimal.valueOf(99.99))
                .quantity(10)
                .category("food")
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("food")
                .build();

        Product product = Product.builder()
                .id(1L)
                .sku("SKU001")
                .name("Test Product 1")
                .price(BigDecimal.valueOf(99.99))
                .quantity(10)
                .category(category)
                .build();

        when(categoryRepository.findByName(any())).thenReturn(Optional.ofNullable(category));
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any())).thenReturn(product);

        ProductResponse productResponse = productService.update(1L, request);

        assertEquals("food", productResponse.getCategory().getName());
        assertEquals("SKU002", productResponse.getSku());
        assertEquals(BigDecimal.valueOf(99.99), productResponse.getPrice());

    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .sku("SKU002")
                .name("Test Product 1")
                .price(BigDecimal.valueOf(99.99))
                .quantity(10)
                .category("nonexistent")
                .build();

        when(categoryRepository.findByName("Test Product 1")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productService.update(1L, request));

        verify(categoryRepository).findByName("Test Product 1");
    }

    @Test
    void  shouldThrowExceptionWhenProductIdNotFound() {
        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .sku("SKU002")
                .name("Test Product 1")
                .price(BigDecimal.valueOf(99.99))
                .quantity(10)
                .category("nonexistent")
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("Test Product 1")
                .build();

        when(categoryRepository.findByName("Test Product 1")).thenReturn(Optional.of(category));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productService.update(1L, request));

        verify(categoryRepository).findByName("Test Product 1");
        verify(productRepository).findById(1L);
    }

    @Test
    void shouldDeleteProductSuccess() {
        Product product = Product.builder()
                .sku("test")
                .name("food")
                .description("")
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));

        productService.delete(1L);
        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }


}
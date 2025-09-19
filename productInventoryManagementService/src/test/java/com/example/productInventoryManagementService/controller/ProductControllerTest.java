package com.example.productInventoryManagementService.controller;

import com.example.productInventoryManagementService.entity.dto.request.ProductCreateRequest;
import com.example.productInventoryManagementService.entity.dto.request.ProductUpdateRequest;
import com.example.productInventoryManagementService.entity.dto.response.CategoryResponse;
import com.example.productInventoryManagementService.entity.dto.response.ProductResponse;
import com.example.productInventoryManagementService.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ProductResponse productResponse;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();

        categoryResponse = CategoryResponse.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic items")
                .build();

        productResponse = ProductResponse.builder()
                .id(1L)
                .sku("SKU001")
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(99.99))
                .cost(BigDecimal.valueOf(50.00))
                .quantity(10)
                .minStockLevel(5)
                .category(categoryResponse)
                .build();
    }

    @Test
    void shouldGetAllProductsSuccessfully() throws Exception {
        List<ProductResponse> productList = Arrays.asList(productResponse);
        when(productService.getAllList()).thenReturn(productList);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sku").value("SKU001"))
                .andExpect(jsonPath("$[0].name").value("Test Product"))
                .andExpect(jsonPath("$[0].price").value(99.99));

        verify(productService).getAllList();
    }

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sku("SKU001")
                .name("Test Product")
                .description("Test Description")
                .price(BigDecimal.valueOf(99.99))
                .cost(BigDecimal.valueOf(50.00))
                .quantity(10)
                .minStockLevel(5)
                .category("Electronics")
                .build();

        when(productService.create(any(ProductCreateRequest.class))).thenReturn(productResponse);

        mockMvc.perform(post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sku").value("SKU001"))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99));

        verify(productService).create(any(ProductCreateRequest.class));
    }

    @Test
    void shouldUpdateProductSuccessfully() throws Exception {
        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .sku("SKU001")
                .name("Updated Product")
                .description("Updated Description")
                .price(BigDecimal.valueOf(149.99))
                .cost(BigDecimal.valueOf(75.00))
                .quantity(15)
                .minStockLevel(8)
                .category("Electronics")
                .build();

        ProductResponse updatedResponse = ProductResponse.builder()
                .id(1L)
                .sku("SKU001")
                .name("Updated Product")
                .description("Updated Description")
                .price(BigDecimal.valueOf(149.99))
                .cost(BigDecimal.valueOf(75.00))
                .quantity(15)
                .minStockLevel(8)
                .category(categoryResponse)
                .build();

        when(productService.update(eq(1L), any(ProductUpdateRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/products/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sku").value("SKU001"))
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(149.99));

        verify(productService).update(eq(1L), any(ProductUpdateRequest.class));
    }

    @Test
    void shouldDeleteProductSuccessfully() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value("Delete Successfully"));

        verify(productService).delete(1L);
    }

    @Test
    void shouldThrowExceptionWhenServiceFails() throws Exception {
        when(productService.getAllList()).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isInternalServerError());

        verify(productService).getAllList();
    }
}
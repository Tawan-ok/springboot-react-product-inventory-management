package com.example.productInventoryManagementService.controller;

import com.example.productInventoryManagementService.entity.dto.request.ProductCreateRequest;
import com.example.productInventoryManagementService.entity.dto.response.ProductResponse;
import com.example.productInventoryManagementService.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    ResponseEntity<List<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllList());
    }

    @PostMapping("/create")
    ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return ResponseEntity.ok(productService.create(request));
    }
}

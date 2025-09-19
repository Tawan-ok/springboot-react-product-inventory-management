package com.example.productInventoryManagementService.service;

import com.example.productInventoryManagementService.entity.Category;
import com.example.productInventoryManagementService.entity.Product;
import com.example.productInventoryManagementService.entity.dto.request.ProductCreateRequest;
import com.example.productInventoryManagementService.entity.dto.request.ProductUpdateRequest;
import com.example.productInventoryManagementService.entity.dto.response.CategoryResponse;
import com.example.productInventoryManagementService.entity.dto.response.ProductResponse;
import com.example.productInventoryManagementService.repository.CategoryRepository;
import com.example.productInventoryManagementService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//- CRUD operations for products
//- Auto-generate SKU if not provided (format: PRD-XXXXX)
//- Search products by name/SKU
//- Filter by category/status
//- Pagination support
//- Calculate profit margin: ((price - cost) / cost) * 100
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    public List<ProductResponse> getAllList() {
        List<Product> products = productRepository.findAll();
        return  products.stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    public ProductResponse create(ProductCreateRequest request) {
        Category category = categoryRepository.findByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product newProduct = Product.builder()
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .cost(request.getCost())
                .quantity(request.getQuantity())
                .minStockLevel(request.getMinStockLevel())
                .category(category)
                .status(request.getStatus())
                .imageUrl(request.getImageUrl())
                .build();

        productRepository.save(newProduct);

        return  mapToProductResponse(newProduct);
    }

    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Category category = categoryRepository.findByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCost(request.getCost());
        product.setQuantity(request.getQuantity());
        product.setMinStockLevel(request.getMinStockLevel());
        product.setMinStockLevel(request.getMinStockLevel());
        product.setCategory(category);
        product.setStatus(request.getStatus());
        product.setImageUrl(request.getImageUrl());

        productRepository.save(product);

        return mapToProductResponse(product);
    }

    public void delete(Long id) {
         productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
         productRepository.deleteById(id);
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .cost(product.getCost())
                .quantity(product.getQuantity())
                .minStockLevel(product.getMinStockLevel())
                .category(mapToCategoryResponse(product.getCategory()))
                .status(product.getStatus())
                .imageUrl(product.getImageUrl())
                .createAt(product.getCreateAt())
                .updateAt(product.getUpdateAt())
                .build();
    }

   private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse
                .builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .build();
    }

    private boolean validateSku(String sku) {
        return sku.startsWith("PRD-");
    }

    private String generateSku(String sku) {
        Product product = productRepository.findBySku(sku);
        int number = 0;
     if (product.getSku() != null) {
         int newNumber = Integer.parseInt(product.getSku());
         newNumber = newNumber + 1;
         String formattedNumber = String.format("%05d", newNumber);
         return  "PRD-" + formattedNumber;
     }
        String formattedNumber = String.format("%05d", number + 1);

        return "PRD-" + formattedNumber;


    }


}

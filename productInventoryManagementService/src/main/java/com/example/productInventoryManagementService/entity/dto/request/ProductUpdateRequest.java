package com.example.productInventoryManagementService.entity.dto.request;

import com.example.productInventoryManagementService.entity.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ProductUpdateRequest {
    @NotBlank(message = "SKU is require")
    private String sku;
    @NotBlank(message = "Name is require")
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal cost;
    private Integer quantity;
    private Integer minStockLevel;
    private String category;
    private Status status;
    private String imageUrl;
}

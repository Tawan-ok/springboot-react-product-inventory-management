package com.example.productInventoryManagementService.entity.dto.response;

import com.example.productInventoryManagementService.entity.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal cost;
    private Integer quantity;
    private Integer minStockLevel;
    private CategoryResponse category;
    private Status status;
    private String imageUrl;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}

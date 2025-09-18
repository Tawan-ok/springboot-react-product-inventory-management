package com.example.productInventoryManagementService.entity.dto.request;

import com.example.productInventoryManagementService.entity.Category;
import com.example.productInventoryManagementService.entity.Status;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ProductCreateRequest {
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

package com.example.productInventoryManagementService.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private Integer quantity;
    private Integer currentStock;
    private String reason;
    private String referenceNumber;
    private String createdBy;
    @CreationTimestamp
    private LocalDateTime createdAt;
}

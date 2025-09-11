package com.example.productInventoryManagementService.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryUpdateRequest {
    @NotBlank(message = "name is require")
    private String name;
    @NotBlank(message = "description is require" )
    private String description;
}

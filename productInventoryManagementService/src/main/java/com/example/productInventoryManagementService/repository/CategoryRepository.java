package com.example.productInventoryManagementService.repository;

import com.example.productInventoryManagementService.entity.Category;
import com.example.productInventoryManagementService.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

}

package com.grocery.store.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productCode; // Unique identifier for products

    @Min(value = 0, message = "Stock must not be negative")
    private int stock;

    private int lowStockThreshold = 10; // Default threshold for notifications
}


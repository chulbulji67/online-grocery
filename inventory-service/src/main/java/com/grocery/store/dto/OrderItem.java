package com.grocery.store.dto;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderItem {

    private String productCode; // Unique code for the product
    private int quantity; // Quantity ordered
}

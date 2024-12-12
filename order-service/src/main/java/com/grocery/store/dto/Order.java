package com.grocery.store.dto;


import com.grocery.store.entity.OrderItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {


    private Long id;

    private String customerName;


    private List<OrderItem> items; // List of items in the order

    private String status; // PENDING, COMPLETED, CANCELLED
}


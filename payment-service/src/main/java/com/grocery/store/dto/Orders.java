package com.grocery.store.dto;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;



@NoArgsConstructor
@AllArgsConstructor
@Component
@Setter
@Getter
@ToString
public class Orders {


    private Long id;

    private String customerName;

    @ElementCollection
    private List<OrderItem> items; // List of items in the order

    private String status; // PENDING, COMPLETED, CANCELLED
}


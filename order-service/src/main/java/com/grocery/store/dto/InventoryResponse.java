package com.grocery.store.dto;


import lombok.Data;

@Data
public class InventoryResponse {
    private String productCode;
    private int stock;
}


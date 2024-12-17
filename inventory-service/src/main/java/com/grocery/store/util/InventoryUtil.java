package com.grocery.store.util;

import com.grocery.store.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryUtil {

    @Autowired
    InventoryRepository inventoryRepository;

    private boolean checkStock(String productCode, int quantity) {


//        if (inventoryResponse == null || inventoryResponse.getStock() < quantity) {
//            return false;
//        }

        return true;
    }
}

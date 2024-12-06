package com.grocery.store.service;



import com.grocery.store.entity.Inventory;
import com.grocery.store.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public Inventory addInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Optional<Inventory> getInventoryByProductCode(String productCode) {
        return inventoryRepository.findByProductCode(productCode);
    }

    public Inventory updateStock(String productCode, int quantity) {
        Inventory inventory = inventoryRepository.findByProductCode(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productCode));
        inventory.setStock(inventory.getStock() - quantity);

        if (inventory.getStock() < inventory.getLowStockThreshold()) {
            notifyLowStock(inventory);
        }

        return inventoryRepository.save(inventory);
    }

    private void notifyLowStock(Inventory inventory) {
        // Stub for notification logic
        System.out.println("Low stock alert for product: " + inventory.getProductCode());
        // Integrate with email or messaging service here
    }
}

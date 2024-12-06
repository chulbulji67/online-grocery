package com.grocery.store.controller;

import com.grocery.store.entity.Inventory;
import com.grocery.store.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryService.addInventory(inventory));
    }

    @GetMapping("/{productCode}")
    public ResponseEntity<Inventory> getInventory(@PathVariable String productCode) {
        Optional<Inventory> inventory = inventoryService.getInventoryByProductCode(productCode);
        return inventory.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update-stock/{productCode}")
    public ResponseEntity<Inventory> updateStock(
            @PathVariable String productCode,
            @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.updateStock(productCode, quantity));
    }
}


package com.grocery.store.service;



import com.grocery.store.dto.NotificationEvent;
import com.grocery.store.dto.OrderItem;
import com.grocery.store.dto.PaymentFailedEvent;
import com.grocery.store.dto.PaymentSuccessEvent;
import com.grocery.store.entity.Inventory;
import com.grocery.store.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
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


    @KafkaListener(topics = "payment-topic", groupId = "inventory-group" , containerFactory = "kafkaListenerContainerFactory")
    public void handlePaymentEvent(NotificationEvent paymentEvent) {
        log.info("Received Payment Event: {}", paymentEvent);

        if (paymentEvent instanceof PaymentSuccessEvent) {
            log.info("Received Payment Event: {}", (PaymentSuccessEvent)paymentEvent);
            if(((PaymentSuccessEvent) paymentEvent).getItems()!=null)
            for (OrderItem item : ((PaymentSuccessEvent) paymentEvent).getItems()) {
                Inventory inventory = updateStock(item.getProductCode(), item.getQuantity());
                if (inventory.getStock() < item.getQuantity()) {
                    log.error("Failed to update stock for product: {}", item.getProductCode());
                }
            }

            log.info("Inventory reduced successfully for Order ID: {}", paymentEvent.getOrderId());

        } else if (paymentEvent instanceof PaymentFailedEvent) {
            log.info("Payment failed for Order ID: {}. Inventory remains unchanged.", paymentEvent.getOrderId());
        }
    }




    private void notifyLowStock(Inventory inventory) {
        // Stub for notification logic
        System.out.println("Low stock alert for product: " + inventory.getProductCode());
        // Integrate with email or messaging service here
    }
}

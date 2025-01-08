package com.grocery.store.service;

import com.grocery.store.dto.InventoryResponse;
import com.grocery.store.entity.Orders;
import com.grocery.store.entity.OrderItem;
import com.grocery.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String INVENTORY_URL = "http://INVENTORY-SERVICE/api/inventory/";

    public Orders placeOrder(Orders orders) {
        // Validate stock for each item
        for (OrderItem item : orders.getItems()) {
            boolean isAvailable = checkStock(item.getProductCode(), item.getQuantity());
            if (!isAvailable) {
                throw new RuntimeException("Insufficient stock for product: " + item.getProductCode());
            }
        }

        // Update order status and save (Not Now After Payment It will be)
//        orders.setStatus("PENDING");
//        Orders savedOrders = orderRepository.save(orders);

        // Deduct stock (Not Now After Payment It will be)
//        for (OrderItem item : savedOrders.getItems()) {
//            deductStock(item.getProductCode(), item.getQuantity());
//        }

//        savedOrders.setStatus("COMPLETED");
        return orders;
    }

    private boolean checkStock(String productCode, int quantity) {
        String url = INVENTORY_URL + productCode;

//        String url = INVENTORY_URL + "/" + productCode;
        InventoryResponse inventoryResponse = restTemplate.getForObject(url, InventoryResponse.class);

        if (inventoryResponse == null || inventoryResponse.getStock() < quantity) {
            return false;
        }

        return true;
    }

    private void deductStock(String productCode, int quantity) {
//        String url = "http://inventory-service/update-stock/" + productCode + "?quantity=" + quantity;
//        restTemplate.put(url, null);
    }

    public Optional<Orders> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}


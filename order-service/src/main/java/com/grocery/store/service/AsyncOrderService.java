package com.grocery.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grocery.store.dto.*;
import com.grocery.store.entity.OrderItem;
import com.grocery.store.entity.Orders;
import com.grocery.store.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AsyncOrderService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrderService orderService;

    // Create Order and Publish Event
    public void createOrder(Order orderRequest) throws JsonProcessingException {

        //Validate it from the Product Service
        List<String> productNames = orderRequest.getItems().stream().map(OrderItem::getProductCode).toList();
        //Url to call the productservice
        String url = "http://product-service/api/products/bulk?productCodes="+String.join(",",productNames);
        ResponseEntity<List<Product>> productResponse= restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        //Check the response is null or is empty
        List<Product> products = productResponse.getBody();
        log.info("Product from product service is:{}",products);
        if(products == null || products.isEmpty()){
            throw new RuntimeException("Your entered product does not match kindly enter correct name");
        }
        double totalPrice = 0.0;
        for(OrderItem orderItem: orderRequest.getItems()){
            log.info(orderItem.getProductCode());
            for(Product product: products){
                log.info(product.getName());
            }
            Product product = products.stream()
                              .filter(prod -> prod.getName().toUpperCase().equals(orderItem.getProductCode().toUpperCase()))
                              .findFirst()
                               .orElseThrow(()->new RuntimeException("Product didn't found"));

            orderItem.setPrice(product.getPrice());
            orderItem.setProductCode(product.getName());
            totalPrice += product.getPrice() * orderItem.getQuantity();

        }

        Orders saveOrder = Orders.builder().customerName(orderRequest.getCustomerName())
                .items(orderRequest.getItems())
                .status("PENDING")
                .price(totalPrice)
                .build();

//        orderRepository.save(saveOrder);
//        1. Check if Product exist in the database or not (Use Order service for this)

        //Orders is Entity
        log.info("Checking if the all Items are present in stock or not");
        Orders order = Orders.builder().id(orderRequest.getId())
                .items(orderRequest.getItems())
                .customerName(orderRequest.getCustomerName())
                .status(orderRequest.getStatus()).build();

        Orders savedOrders = orderService.placeOrder(order);

        //Changing to Order so that can be sent to order-topic


        //Saving into the database
//        log.info("Id Of saved order before payment {} price={}",savedOrders.getId(),savedOrders.getPrice());
        savedOrders = orderRepository.save(saveOrder);
        log.info("Id Of saved order before payment {} price={}",savedOrders.getId(),savedOrders.getPrice());

        Order processedOrder = Order.builder().id(savedOrders.getId())
                .items(savedOrders.getItems())
                .customerName(savedOrders.getCustomerName())
                .status(savedOrders.getStatus()).build();

        log.info("event sent to Order topic ,Payment Processing");
        kafkaTemplate.send("order-topic", processedOrder);

        //Deduct from inventory if Payment success

        // Save order details to the database (optional)

        log.info("Processed Order is going ");


    }

    // Listen for Payment Status Updates
    @KafkaListener(topics = "payment-topic", groupId = "order-group", containerFactory = "concurrentKafkaListenerContainerFactory")
    public void handlePaymentResponse(NotificationEvent paymentResponse) throws JsonProcessingException {
//        PaymentResponse response = new ObjectMapper().readValue(paymentResponse, PaymentResponse.class);
        log.info("Event Consumed by Order Service {}",paymentResponse.toString());
        if (paymentResponse instanceof PaymentSuccessEvent) {
            PaymentSuccessEvent paymentSuccessEvent = (PaymentSuccessEvent) paymentResponse;
            // Update order status to COMPLETED
            NotificationEvent notificationEvent = new PaymentSuccessEvent((paymentSuccessEvent.getOrderId()+""),
                    "chulbulji67@gmail.com","Your payment is Success", paymentSuccessEvent.getStatus());

            //Update the Order also in the database
            Orders orders = orderRepository.findById(Long.parseLong(paymentResponse.getOrderId())).orElseThrow(()->new RuntimeException("Product Not Found"));
            orders.setStatus("SUCCESS");
            log.info("Id Of saved order before payment {},{}",orders.getId(),orders.getPrice());
            orderRepository.save(orders);
            //Send Notification That Payment is success
            kafkaTemplate.send("notification-topic", notificationEvent);
            log.info("Your Order Placed");
            System.out.println("Order " + paymentResponse.getOrderId() + " completed successfully!");
        } else {
            // Handle payment failure
            log.warn("Payment Failed");
            PaymentFailedEvent paymentFailedEvent = (PaymentFailedEvent) paymentResponse;
            NotificationEvent notificationEvent = new PaymentFailedEvent((paymentResponse.getOrderId()+""),
                    "chulbulji67@gmail.com","Your Payment Failed", paymentFailedEvent.getStatus());

            //Change in database also
            //Update the Order also in the database
            Orders orders = orderRepository.findById(Long.parseLong(paymentResponse.getOrderId())).orElseThrow(()->new RuntimeException("Product Not Found"));
            orders.setStatus("CANCEL");
            log.info("Id Of saved order before payment {},{}",orders.getId(),orders.getPrice());
            orderRepository.save(orders);
            //Send Notification That Payment is success
            kafkaTemplate.send("notification-topic", notificationEvent);
            log.info("Your Order Can't be placed");
            System.out.println("Payment failed for order " + paymentResponse.getOrderId());
        }
    }
}

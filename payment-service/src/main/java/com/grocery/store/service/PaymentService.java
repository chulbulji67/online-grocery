package com.grocery.store.service;

import com.grocery.store.entity.Payment;
import com.grocery.store.repository.PaymentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String PAYMENT_STATUS_TOPIC = "payment-status-updates";



    @Autowired
    RestTemplate restTemplate;

    @Retry(name = "default1")
    @CircuitBreaker(name = "default1", fallbackMethod = "fallbackMethod")
    @RateLimiter(name = "default1")
    public Payment processPayment(Payment payment) throws Exception{

        log.info("Process Payment called");
        payment.setStatus("PENDING");
        Payment savedPayment = paymentRepository.save(payment);

        // Simulate a mock payment gateway processing
        boolean isSuccess = mockPaymentGateway();

        // Artificially induce a failure to simulate a fallback scenario
        if (!isSuccess) {
            throw new RuntimeException("Payment gateway failed");
        }

        // Update payment status
        savedPayment.setStatus(isSuccess ? "SUCCESS" : "FAILED");
        paymentRepository.save(savedPayment);

        // Notify the status asynchronously
        notifyPaymentStatus(savedPayment);

        return savedPayment;
    }

    private boolean mockPaymentGateway() {
//         Simulate payment success or failure randomly
        return new Random().nextBoolean();
//        return false;
    }

    private void notifyPaymentStatus(Payment payment) {
        String message = String.format("Payment for Order ID %d is %s", payment.getOrderId(), payment.getStatus());
//        kafkaTemplate.send(PAYMENT_STATUS_TOPIC, message);
        System.out.println("Payment status sent to Kafka: " + message);
    }

    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found!"));
    }

    public static void fallbackMethod(){
        log.info("Fall Back Method invoked");
    }
}

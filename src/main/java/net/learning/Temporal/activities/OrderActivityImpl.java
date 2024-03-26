package net.learning.Temporal.activities;

import lombok.extern.slf4j.Slf4j;
import net.learning.Temporal.api.OrderRequest;

@Slf4j
public class OrderActivityImpl implements OrderActivity {

    public void placeOrder(String orderId) {
        log.info("Order placed Successfully with orderID : {}", orderId);
    }

    public void processPayment(OrderRequest orderRequest) {
        orderRequest.setPaymentStatus("COMPLETED");
        log.info("Payment {} for OrderID : {}", orderRequest.getPaymentStatus(), orderRequest.getOrderId());
    }

    public void completeOrder(String orderId) {
        log.info("Order closed for orderID : {}", orderId);
    }
}

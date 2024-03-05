package net.learning.Temporal.api;

import lombok.Data;

@Data
public class OrderRequest {

    private String orderId;
    private String paymentStatus;
}

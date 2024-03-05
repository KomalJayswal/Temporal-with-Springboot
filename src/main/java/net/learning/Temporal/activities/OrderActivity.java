package net.learning.Temporal.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import net.learning.Temporal.api.OrderRequest;

@ActivityInterface
public interface OrderActivity {

    @ActivityMethod
    void placeOrder(String orderId);

    @ActivityMethod
    void processPayment(OrderRequest orderRequest);

    @ActivityMethod
    void completeOrder(String orderId);
}

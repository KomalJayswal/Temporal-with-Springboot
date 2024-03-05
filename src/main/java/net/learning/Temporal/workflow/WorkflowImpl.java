package net.learning.Temporal.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import net.learning.Temporal.activities.OrderActivity;
import net.learning.Temporal.api.OrderRequest;

import java.time.Duration;

public class WorkflowImpl implements Workflow {

    private final RetryOptions retryoptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(100))
            .setBackoffCoefficient(2)
            .setMaximumAttempts(500)
            .build();
    private final ActivityOptions options = ActivityOptions.newBuilder()
            // Timeout options specify when to automatically timeout Activities if the process is taking too long.
            .setStartToCloseTimeout(Duration.ofSeconds(5))
            // Optionally provide customized RetryOptions.
            // Temporal retries failures by default, this is simply an example.
            .setRetryOptions(retryoptions)
            .build();

    private final OrderActivity orderActivity = io.temporal.workflow.Workflow.newActivityStub(OrderActivity.class, options);

    /**
     * Process the Order.
     *
     * @param orderRequest order details that is requested.
     */
    public void processOrder(OrderRequest orderRequest) {
        orderActivity.placeOrder(orderRequest.getOrderId());
        orderActivity.processPayment(orderRequest);
        orderActivity.completeOrder(orderRequest.getOrderId());
    }
}

package net.learning.Temporal.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import net.learning.Temporal.api.OrderRequest;

@WorkflowInterface
public interface Workflow {

    @WorkflowMethod
    void processOrder(OrderRequest orderRequest);

}

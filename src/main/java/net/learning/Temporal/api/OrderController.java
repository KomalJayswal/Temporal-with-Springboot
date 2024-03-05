package net.learning.Temporal.api;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import net.learning.Temporal.activities.OrderActivityImpl;
import net.learning.Temporal.workflow.Workflow;
import net.learning.Temporal.workflow.WorkflowImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {

    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderRequest orderRequest) {

        WorkflowServiceStubs service = WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions.newBuilder()
                //.setEnableHttps(false)
                //.setTarget("192.168.100.5:7233")
                .build());
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("TaskQueue");
        worker.registerWorkflowImplementationTypes(WorkflowImpl.class);
        // Activities are stateless and thread safe so a shared instance is used.
        worker.registerActivitiesImplementations(new OrderActivityImpl());

        // Start listening to the Task Queue.
        factory.start();

        // WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("TaskQueue")
                .setWorkflowId("WorkflowId") // If we do not provide this, then  it will go empty and temporal sdk will create a new UUID
                .build();
        //WorkflowClient client = WorkflowClient.newInstance(service);
        Workflow workflow = client.newWorkflowStub(Workflow.class, options);
        WorkflowExecution we = WorkflowClient.start(workflow::processOrder, orderRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // client -> factory -> worker or taskqueue -> activity

    // client creates new workflow

    // factory starts the workflow

}
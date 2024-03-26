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

    /**
     * service -> creates client -> creates factory -> creates worker on taskqueue -> registers 1. WF type 2. activities Impl
     * <p>
     * options -> starts new workflow execution
     */
    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderRequest orderRequest) {

        //creates a new instance of WorkflowServiceStubs,which is part of the Temporal SDK, used to communicate with the Temporal service.
        WorkflowServiceStubs service = WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions.newBuilder()
                .build());
        //creates a new instance of WorkflowClient, used to interact with workflows and activities in the Temporal service.
        WorkflowClient client = WorkflowClient.newInstance(service);
        //creates a new instance of WorkerFactory, which is used to create workers that listen for and execute workflow tasks.
        WorkerFactory factory = WorkerFactory.newInstance(client);
        //creates a worker instance that listens for tasks on a specific task queue named "TaskQueue".
        Worker worker = factory.newWorker("TaskQueue");
        //registers the implementation class for the workflow type. WorkflowImpl.class is the class that implements the workflow logic.
        worker.registerWorkflowImplementationTypes(WorkflowImpl.class);
        //registers the implementation class for activities. OrderActivityImpl is the class that implements the activities used by the workflow.
        // Activities are stateless and thread safe so a shared instance is used.
        worker.registerActivitiesImplementations(new OrderActivityImpl());

        //starts the worker factory, causing the worker to begin listening for tasks on the specified task queue.
        factory.start();

        //creates options for starting a new workflow execution. It specifies the task queue and workflow ID to use.
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("TaskQueue")
                .setWorkflowId("WorkflowId") // If we do not provide WorkflowId, then it will go empty and temporal sdk will create a new UUID itself
                .build();


        /*
        newWorkflowStub : used to create a new stub for interacting with a specific workflow type.

        Workflow.class: This parameter specifies the interface or class that defines the workflow type.
                        In our case, Workflow.class indicates that the stub being created will be capable of interacting with workflows
                        that implement the Workflow interface. It's important to note that the provided class should match the interface
                        or class used to define the workflow logic.

        options : include configuration settings such as the task queue, workflow ID, workflow timeout,
                  and other parameters related to workflow execution.
         */

        Workflow workflow = client.newWorkflowStub(Workflow.class, options);
        /*
        WorkflowClient.start() : used to start a new workflow execution.
        processOrder : the entry point for the workflow logic that needs to be executed
        orderRequest : input that will be provided to the workflow during its execution
        */
        WorkflowExecution we = WorkflowClient.start(workflow::processOrder, orderRequest);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
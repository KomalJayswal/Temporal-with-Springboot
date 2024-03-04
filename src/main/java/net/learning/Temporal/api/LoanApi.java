package net.learning.Temporal.api;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import net.learning.Temporal.activities.BureauCheckActivityImpl;
import net.learning.Temporal.activities.DecisionActivityImpl;
import net.learning.Temporal.activities.UnderwritingActivityImpl;
import net.learning.Temporal.workflow.LoanApplicationWorkflow;
import net.learning.Temporal.workflow.LoanApplicationWorkflowImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/loans")
public class LoanApi {
    @PostMapping("/loanApplication")
    public ResponseEntity loanApplication(@RequestBody LoanApplication loanApplication) {

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("TaskQueue");
        worker.registerWorkflowImplementationTypes(LoanApplicationWorkflowImpl.class);
        // Activities are stateless and thread safe so a shared instance is used.
        worker.registerActivitiesImplementations(new BureauCheckActivityImpl());
        worker.registerActivitiesImplementations(new UnderwritingActivityImpl());
        worker.registerActivitiesImplementations(new DecisionActivityImpl());
        // Start listening to the Task Queue.
        factory.start();

       // WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("TaskQueue")
                .setWorkflowId("WorkflowId")
                .build();
        //WorkflowClient client = WorkflowClient.newInstance(service);
        LoanApplicationWorkflow workflow = client.newWorkflowStub(LoanApplicationWorkflow.class,options);
        WorkflowExecution we = WorkflowClient.start(workflow::applyForALoan,loanApplication.getLoanNo(),loanApplication.getSsn(),loanApplication.getLoanAmount());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // client -> factory -> worker or taskqueue -> activity

    // client creates new workflow

    // client starts the workflow

}
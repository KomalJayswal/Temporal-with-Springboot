package net.learning.Temporal.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface LoanApplicationWorkflow {

    @WorkflowMethod
    void applyForALoan(
            String loanNumber,
            String ssn,
            double amount);

}

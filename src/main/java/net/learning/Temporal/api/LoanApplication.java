package net.learning.Temporal.api;

import lombok.Data;

@Data
public class LoanApplication {

    private String loanNo;
    private double loanAmount;
    private String ssn;
}

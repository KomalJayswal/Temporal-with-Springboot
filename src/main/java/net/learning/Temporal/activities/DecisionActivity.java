package net.learning.Temporal.activities;


import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface DecisionActivity {
    void decideOnLoan(String loanNumber, double amount);
}

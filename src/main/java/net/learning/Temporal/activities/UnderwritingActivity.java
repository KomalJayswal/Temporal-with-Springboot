package net.learning.Temporal.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface UnderwritingActivity {
    void underwriteLoan(String loanNumber,double amount);
}

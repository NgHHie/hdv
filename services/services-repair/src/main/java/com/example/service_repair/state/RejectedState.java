package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class RejectedState implements RepairState {
    @Override
    public void next(RepairContext context) {
        // Cannot move next from a rejected state
        throw new IllegalStateException("Cannot proceed with rejected repair");
    }

    @Override
    public void prev(RepairContext context) {
        // Cannot go back from a rejected state
        throw new IllegalStateException("Cannot revert rejected repair");
    }

    @Override
    public void cancel(RepairContext context) {
        // Already in a terminal state
        throw new IllegalStateException("Cannot cancel rejected repair");
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.REJECTED;
    }
}
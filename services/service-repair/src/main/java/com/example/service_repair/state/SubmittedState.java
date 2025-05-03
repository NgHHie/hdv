package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

// Concrete State implementations
class SubmittedState implements RepairState {
    @Override
    public void next(RepairContext context) {
        context.setCurrentState(new ReceivedState());
    }

    @Override
    public void prev(RepairContext context) {
        // Cannot go back from initial state
        throw new IllegalStateException("Cannot move back from initial state");
    }

    @Override
    public void cancel(RepairContext context) {
        context.setCurrentState(new CancelledState());
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.SUBMITTED;
    }
}
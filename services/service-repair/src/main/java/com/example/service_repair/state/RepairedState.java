package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class RepairedState implements RepairState {
    @Override
    public void next(RepairContext context) {
        context.setCurrentState(new TestingState());
    }

    @Override
    public void prev(RepairContext context) {
        context.setCurrentState(new RepairingState());
    }

    @Override
    public void cancel(RepairContext context) {
        // Too late to cancel at this stage
        throw new IllegalStateException("Cannot cancel repair at this stage");
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.REPAIRED;
    }
}

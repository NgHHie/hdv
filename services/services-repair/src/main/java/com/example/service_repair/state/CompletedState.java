package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class CompletedState implements RepairState {
    @Override
    public void next(RepairContext context) {
        // Final state, cannot move forward
        throw new IllegalStateException("Repair process already completed");
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.COMPLETED;
    }
}

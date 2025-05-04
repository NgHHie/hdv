package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class DeliveredState implements RepairState {
    @Override
    public void next(RepairContext context) {
        // Final state, cannot move forward
        throw new IllegalStateException("Repair process already completed");
    }

    @Override
    public void prev(RepairContext context) {
        context.setCurrentState(new DeliveringState());
    }

    @Override
    public void cancel(RepairContext context) {
        // Too late to cancel at this stage
        throw new IllegalStateException("Cannot cancel completed repair");
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.DELIVERED;
    }
}
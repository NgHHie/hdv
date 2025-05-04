package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class CancelledState implements RepairState {
    @Override
    public void next(RepairContext context) {
        // Cannot move next from a cancelled state
        throw new IllegalStateException("Cannot proceed with cancelled repair");
    }

    @Override
    public void prev(RepairContext context) {
        // Cannot go back from a cancelled state
        throw new IllegalStateException("Cannot revert cancelled repair");
    }

    @Override
    public void cancel(RepairContext context) {
        // Already cancelled
        throw new IllegalStateException("Repair is already cancelled");
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.CANCELLED;
    }
}

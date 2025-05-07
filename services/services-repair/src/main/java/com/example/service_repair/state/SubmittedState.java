package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

// Concrete State implementations
class SubmittedState implements RepairState {
    @Override
    public void next(RepairContext context) {
        context.setCurrentState(new ReceivedState());
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.SUBMITTED;
    }
}

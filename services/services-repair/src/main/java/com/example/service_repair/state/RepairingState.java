package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class RepairingState implements RepairState {
    @Override
    public void next(RepairContext context) {
        context.setCurrentState(new CompletedState());
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.REPAIRING;
    }
}

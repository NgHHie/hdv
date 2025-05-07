package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class DiagnosingState implements RepairState {
    @Override
    public void next(RepairContext context) {
        context.setCurrentState(new RepairingState());
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.DIAGNOSING;
    }
}
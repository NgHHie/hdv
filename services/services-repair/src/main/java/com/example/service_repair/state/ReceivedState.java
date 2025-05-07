package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class ReceivedState implements RepairState {
    @Override
    public void next(RepairContext context) {
        context.setCurrentState(new DiagnosingState());
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.RECEIVED;
    }
}
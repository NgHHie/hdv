package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class ReceivedState implements RepairState {
    @Override
    public void next(RepairContext context) {
        context.setCurrentState(new UnderDiagnosisState());
    }

    @Override
    public void prev(RepairContext context) {
        context.setCurrentState(new SubmittedState());
    }

    @Override
    public void cancel(RepairContext context) {
        context.setCurrentState(new CancelledState());
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.RECEIVED;
    }
}
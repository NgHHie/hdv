package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class UnderDiagnosisState implements RepairState {
    @Override
    public void next(RepairContext context) {
        context.setCurrentState(new DiagnosingState());
    }

    @Override
    public void prev(RepairContext context) {
        context.setCurrentState(new ReceivedState());
    }

    @Override
    public void cancel(RepairContext context) {
        context.setCurrentState(new CancelledState());
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.UNDER_DIAGNOSIS;
    }
}
package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

class DiagnosingState implements RepairState {
    @Override
    public void next(RepairContext context) {
        // Check if it's within warranty
        if (Boolean.TRUE.equals(context.getRepairRequest().getWithinWarranty())) {
            context.setCurrentState(new RepairingState());
        } else {
            context.setCurrentState(new WaitingApprovalState());
        }
    }

    @Override
    public void prev(RepairContext context) {
        context.setCurrentState(new UnderDiagnosisState());
    }

    @Override
    public void cancel(RepairContext context) {
        context.setCurrentState(new CancelledState());
    }

    @Override
    public RepairStatus getStatus() {
        return RepairStatus.DIAGNOSING;
    }
}
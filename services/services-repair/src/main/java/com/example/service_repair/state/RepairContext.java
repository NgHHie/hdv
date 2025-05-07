package com.example.service_repair.state;

import com.example.service_repair.models.RepairRequest;
import com.example.service_repair.constants.RepairStatus;

public class RepairContext {
    private RepairState currentState;
    private RepairRequest repairRequest;

    public RepairContext(RepairRequest repairRequest) {
        this.repairRequest = repairRequest;
        setInitialState();
    }

    private void setInitialState() {
        switch (repairRequest.getStatus()) {
            case SUBMITTED:
                this.currentState = new SubmittedState();
                break;
            case RECEIVED:
                this.currentState = new ReceivedState();
                break;
            case DIAGNOSING:
                this.currentState = new DiagnosingState();
                break;
            case REPAIRING:
                this.currentState = new RepairingState();
                break;
            case COMPLETED:
                this.currentState = new CompletedState();
                break;
            default:
                this.currentState = new SubmittedState();
                break;
        }
    }

    public void setCurrentState(RepairState state) {
        this.currentState = state;
        this.repairRequest.setStatus(state.getStatus());
    }

    public RepairState getCurrentState() {
        return currentState;
    }

    public RepairRequest getRepairRequest() {
        return repairRequest;
    }

    public void moveToNextState() {
        currentState.next(this);
    }
}
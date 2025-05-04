package com.example.service_repair.state;

import com.example.service_repair.models.RepairRequest;

// Context class that maintains a reference to the current state
public class RepairContext {
    private RepairState currentState;
    private RepairRequest repairRequest;

    public RepairContext(RepairRequest repairRequest) {
        this.repairRequest = repairRequest;
        // Set initial state based on the repair request's status
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
            case UNDER_DIAGNOSIS:
                this.currentState = new UnderDiagnosisState();
                break;
            case DIAGNOSING:
                this.currentState = new DiagnosingState();
                break;
            case WAITING_APPROVAL:
                this.currentState = new WaitingApprovalState();
                break;
            case REPAIRING:
                this.currentState = new RepairingState();
                break;
            case REPAIRED:
                this.currentState = new RepairedState();
                break;
            case TESTING:
                this.currentState = new TestingState();
                break;
            case COMPLETED:
                this.currentState = new CompletedState();
                break;
            case DELIVERING:
                this.currentState = new DeliveringState();
                break;
            case DELIVERED:
                this.currentState = new DeliveredState();
                break;
            case CANCELLED:
                this.currentState = new CancelledState();
                break;
            case REJECTED:
                this.currentState = new RejectedState();
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

    public void moveToPreviousState() {
        currentState.prev(this);
    }

    public void cancelRepair() {
        currentState.cancel(this);
    }
}

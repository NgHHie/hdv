package com.example.service_repair.state;

import com.example.service_repair.constants.RepairStatus;

public interface RepairState {
    void next(RepairContext context);
    void prev(RepairContext context);
    void cancel(RepairContext context);
    RepairStatus getStatus();
}
package com.nexi.model;

import java.time.LocalDateTime;

public class OperationResponse {

    private LocalDateTime operationTime;
    private String status;

    public OperationResponse(LocalDateTime operationTime, String status) {
        this.operationTime = operationTime;
        this.status = status;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public String getStatus() {
        return status;
    }
}

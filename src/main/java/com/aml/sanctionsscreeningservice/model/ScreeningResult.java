package com.aml.sanctionsscreeningservice.model;


import java.time.Instant;

public class ScreeningResult {
    private String userId;
    private String screeningId;
    private ScreeningStatus status;
    private String externalTransactionId;
    private Instant screenedAt;

    public String getExternalTransactionId() {
        return externalTransactionId;
    }

    public void setExternalTransactionId(String externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ScreeningStatus getStatus() {
        return status;
    }

    public void setStatus(ScreeningStatus status) {
        this.status = status;
    }

    public String getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(String screeningId) {
        this.screeningId = screeningId;
    }

    public Instant getScreenedAt() {
        return screenedAt;
    }

    public void setScreenedAt(Instant screenedAt) {
        this.screenedAt = screenedAt;
    }
}

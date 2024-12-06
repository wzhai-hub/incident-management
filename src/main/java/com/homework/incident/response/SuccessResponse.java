package com.homework.incident.response;

public class SuccessResponse {
    private final long accidentId;
    private final String message;

    public SuccessResponse(long accidentId, String message) {
        this.accidentId = accidentId;
        this.message = message;
    }

    public long getAccidentId() {
        return accidentId;
    }

    public String getMessage() {
        return message;
    }
}

package com.medrec.utils;

public class ResponseMessage {
    private boolean isSuccessful;
    private String message;

    public ResponseMessage(boolean isSuccessful, String message) {
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "isSuccessful=" + isSuccessful + ", message='" + message;
    }
}

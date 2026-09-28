package com.api.models.response;

public class AuthErrorResponse {

    private String reason;
    public AuthErrorResponse(){

    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "AuthErrorResponse{" +
                "reason='" + reason + '\'' +
                '}';
    }

    public AuthErrorResponse(String reason) {
        this.reason = reason;
    }



}

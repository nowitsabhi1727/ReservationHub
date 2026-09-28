package com.api.models.response;

public class AuthResponse {


    private String token;
    public AuthResponse(){

    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='" + token + '\'' +
                '}';
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthResponse(String token) {
        this.token = token;
    }
}

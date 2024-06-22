package com.shop.mob.auth;

public class AuthResponse {
    
    private boolean success;
    private String message;
    private String token;

    public AuthResponse(String message, boolean success, String data) {
        this.token = data;
        this.message = message;
        this.success = success;
    }

    public AuthResponse(String message, boolean success){
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return token;
    }

    public void setData(String data) {
        this.token = data;
    }
}

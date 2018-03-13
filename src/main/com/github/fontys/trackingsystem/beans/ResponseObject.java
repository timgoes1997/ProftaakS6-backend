package com.github.fontys.trackingsystem.beans;

public class ResponseObject {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseObject(String message) {
        this.message = message;
    }
}

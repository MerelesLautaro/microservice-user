package com.lautadev.microservice_user.Throwable;

public class UserException extends RuntimeException{
    public UserException(String message) {
        super(message);
    }
}

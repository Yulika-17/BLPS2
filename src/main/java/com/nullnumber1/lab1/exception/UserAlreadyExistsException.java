package com.nullnumber1.lab1.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("User with username [" + username + "] is already exists");
    }
}

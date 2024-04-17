package com.nullnumber1.lab1.exception.not_found;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String username) {
        super("user", "username", username);
    }
    public UserNotFoundException(Long userId) {
        super("user", "id", String.valueOf(userId));
    }
}

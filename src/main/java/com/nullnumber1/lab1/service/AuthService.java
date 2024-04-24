package com.nullnumber1.lab1.service;

import com.nullnumber1.lab1.dto.RegisterRequest;
import com.nullnumber1.lab1.exception.UserAlreadyExistsException;
import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.repository.UserRepository;
import com.nullnumber1.lab1.util.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request, RoleEnum roleEnum) {
        if (userRepository.findByUsername(request.getUsername()) != null){
            throw new UserAlreadyExistsException(request.getUsername());
        }
        User user = new User(
                null,
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                Set.of(roleEnum)
        );

        userRepository.save(user);
        return user;
    }
}
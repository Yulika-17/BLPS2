package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.dto.RegisterRequest;
import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.service.AuthService;
import com.nullnumber1.lab1.util.enums.RoleType;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Register management")
public class AuthController {
    private AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(
            @Validated @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request, RoleType.CUSTOMER));
    }

    @Hidden
    @PostMapping(path = "/admin/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registerAdmin(
            @Validated @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request, RoleType.ADMIN));
    }
}

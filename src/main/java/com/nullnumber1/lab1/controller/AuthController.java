package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.dto.RegisterRequest;
import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.service.AuthService;
import com.nullnumber1.lab1.util.enums.RoleEnum;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "Register management")
public class AuthController {
  private AuthService authService;

  @PostMapping(
      path = "/register",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> register(@Validated @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request, RoleEnum.CUSTOMER));
  }

  @Hidden
  @PreAuthorize("hasAuthority('REGISTER_OTHER_ADMINS')")
  @PostMapping(
      path = "/admin/register",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> registerAdmin(@Validated @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request, RoleEnum.ADMIN));
  }
}

package com.nullnumber1.lab1.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  @NotBlank(message = "Username is required.")
  private String username;

  @NotBlank(message = "Password is required.")
  private String password;

  @Email(message = "Невалидный mail")
  @NotBlank(message = "Email is required.")
  private String email;
}

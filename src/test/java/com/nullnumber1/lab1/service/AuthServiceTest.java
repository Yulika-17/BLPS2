package com.nullnumber1.lab1.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nullnumber1.lab1.dto.RegisterRequest;
import com.nullnumber1.lab1.exception.UserAlreadyExistsException;
import com.nullnumber1.lab1.model.*;
import com.nullnumber1.lab1.repository.UserRepository;
import com.nullnumber1.lab1.util.enums.RoleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private AuthService authService;

  @Test
  public void testRegister_NewUser() {
    RegisterRequest request = new RegisterRequest("newuser", "password", "newuser@example.com");
    when(userRepository.findByUsername("newuser")).thenReturn(null);
    when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

    User user = authService.register(request, RoleEnum.CUSTOMER);

    assertNotNull(user);
    assertEquals("newuser", user.getUsername());
    assertEquals("encodedPassword", user.getPassword());
    assertEquals("newuser@example.com", user.getEmail());
    assertTrue(user.getRoles().contains(RoleEnum.CUSTOMER));

    verify(userRepository).save(any(User.class));
  }

  @Test
  public void testRegister_UserAlreadyExists() {
    RegisterRequest request =
        new RegisterRequest("existinguser", "password", "existinguser@example.com");
    User existingUser = new User();
    when(userRepository.findByUsername("existinguser")).thenReturn(existingUser);

    assertThrows(
        UserAlreadyExistsException.class, () -> authService.register(request, RoleEnum.CUSTOMER));

    verify(userRepository, never()).save(any(User.class));
  }
}

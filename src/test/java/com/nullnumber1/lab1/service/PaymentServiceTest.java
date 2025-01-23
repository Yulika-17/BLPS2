package com.nullnumber1.lab1.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nullnumber1.lab1.exception.PaymentIsNotProcessedException;
import com.nullnumber1.lab1.exception.not_found.UserNotFoundException;
import com.nullnumber1.lab1.model.*;
import com.nullnumber1.lab1.repository.PaymentRepository;
import com.nullnumber1.lab1.repository.UserRepository;
import com.nullnumber1.lab1.service.kafka.ProducerService;
import com.nullnumber1.lab1.util.enums.PaymentStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class PaymentServiceTest {

  @Mock private PaymentRepository paymentRepository;

  @Mock private UserRepository userRepository;

  @Mock private ProducerService producerService;

  @InjectMocks private PaymentService paymentService;

  private User testUser;
  private Payment testPayment;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testUser = new User();
    testUser.setId(1L);
    testUser.setEmail("test@example.com");

    testPayment = new Payment();
    testPayment.setId(1L);
    testPayment.setUser(testUser);
    testPayment.setStatus(PaymentStatus.INITIATED.name());
  }

  @Test
  void testCreatePayment_Success() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

    Long paymentId = paymentService.createPayment(1L);

    assertNotNull(paymentId);
    assertEquals(testPayment.getId(), paymentId);
  }

  @Test
  void testCreatePayment_IncompletePayments() {
    when(paymentRepository.findAllByUserId(1L)).thenReturn(List.of(testPayment));

    Long paymentId = paymentService.createPayment(1L);

    assertEquals(-1L, paymentId);
  }

  @Test
  void testGetPaymentStatus_Success() {
    when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

    PaymentStatus status = paymentService.getPaymentStatus(1L);

    assertEquals(PaymentStatus.INITIATED, status);
  }

  @Test
  void testUpdatePaymentStatus_Success() {
    when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

    paymentService.updatePaymentStatus(1L, PaymentStatus.PROCESSED);

    assertEquals(PaymentStatus.PROCESSED.name(), testPayment.getStatus());
  }

  @Test
  void testUpdatePaymentType_Success() {
    when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

    paymentService.updatePaymentType(1L, "Credit Card", 100.0);

    assertEquals("Credit Card", testPayment.getPaymentType().getType());
    assertEquals(100.0, testPayment.getPaymentType().getAmount().doubleValue());
  }

  @Test
  void testUpdateOKTMO_Success() {
    when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

    paymentService.updateOKTMO(1L, "123456");

    assertEquals("123456", testPayment.getOktmo().getCode());
  }

  @Test
  void testProcessPayment_Success() {
    when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

    boolean result = paymentService.processPayment(1L, "Credit Card");

    assertTrue(result);
    assertEquals(PaymentStatus.PROCESSED.name(), testPayment.getStatus());

    verify(paymentRepository, times(1)).save(testPayment);
  }

  @Test
  void testReviewPayment_Approve() {
    Payer testPayer = new Payer(1L, "Test Payer", 123456789012L);
    testPayment.setPayer(testPayer);
    testPayment.setForSelf(true);
    OKTMO testOktmo = new OKTMO(1L, "4500000");
    testPayment.setOktmo(testOktmo);
    PaymentType testPaymentType = new PaymentType(1L, "Gosposhlina1", new BigDecimal(600));
    testPayment.setPaymentType(testPaymentType);
    when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

    testPayment.setStatus(PaymentStatus.PROCESSED.name());

    when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
    PaymentStatus result = paymentService.reviewPayment(1L, true);

    assertEquals(PaymentStatus.APPROVED, result);
    verify(producerService).send(eq("mail-topic"), eq(testUser.getEmail()));
  }

  @Test
  void testReviewPayment_Fail() {
    when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

    testPayment.setStatus(PaymentStatus.PROCESSED.name());

    when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
    PaymentStatus result = paymentService.reviewPayment(1L, false);

    assertEquals(PaymentStatus.FAILED, result);
    verify(producerService).send(eq("mail-topic"), eq(testUser.getEmail()));
  }

  @Test
  void testIsCurrentUserPaymentCreator() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(testUser);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

    boolean result = paymentService.isCurrentUserPaymentCreator(1L);

    assertTrue(result);
  }

  @Test
  void testIsNotCurrentUserPaymentCreator() {
    User anotherUser = new User();
    anotherUser.setId(2L);
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(anotherUser);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

    boolean result = paymentService.isCurrentUserPaymentCreator(1L);

    assertFalse(result);
  }

  @Test
  void testCreatePayment_UserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () -> {
          paymentService.createPayment(1L);
        });
  }

  @Test
  void testReviewPayment_PaymentNotProcessed() {
    when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

    testPayment.setStatus(PaymentStatus.INITIATED.name());

    assertThrows(
        PaymentIsNotProcessedException.class,
        () -> {
          paymentService.reviewPayment(1L, true);
        });
  }
}

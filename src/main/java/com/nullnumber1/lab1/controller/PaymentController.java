package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.exception.CreatePaymentException;
import com.nullnumber1.lab1.exception.DocumentGenerationException;
import com.nullnumber1.lab1.exception.PaymentIsNotProcessedException;
import com.nullnumber1.lab1.model.PaymentDocument;
import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.service.PaymentService;
import com.nullnumber1.lab1.service.PdfGenerationService;
import com.nullnumber1.lab1.util.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payment management")
@AllArgsConstructor
@Slf4j
@SecurityRequirement(name = "basicAuth")
public class PaymentController {

  private final PaymentService paymentService;

  private final PdfGenerationService pdfGenerationService;

  @PreAuthorize("hasAuthority('CREATE_PAYMENT')")
  @PostMapping("/init")
  @Operation(
      description = "Create payment",
      responses = {
        @ApiResponse(responseCode = "200", description = "Payment was successfully created"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "401", description = "User is not authed")
      })
  public ResponseEntity<Long> initPayment() {
    log.info("Received request to create payment for current user");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    Long userId = user.getId();
    log.info("initPayment: userId={}", userId);
    Long paymentId = paymentService.createPayment(userId);
    if (paymentId == -1L) {
      throw new CreatePaymentException();
    }
    return ResponseEntity.ok(paymentId);
  }

  @PreAuthorize("hasAuthority('VIEW_PAYMENT_STATUS')")
  @GetMapping("/{paymentId}")
  @Operation(
      description = "Get payment status",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Payment status was successfully received"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "401", description = "User is not authed")
      })
  public ResponseEntity<PaymentStatus> getPaymentStatus(
      @PathVariable(value = "paymentId") Long paymentId) {
    PaymentStatus status = paymentService.getPaymentStatus(paymentId);
    return ResponseEntity.ok(status);
  }

  @PreAuthorize("hasAuthority('FILL_PAYMENT_TYPE_AMOUNT')")
  @PostMapping("/{paymentId}/type")
  @Operation(
      description = "Fill the payment type",
      responses = {
        @ApiResponse(responseCode = "200", description = "Payment type was successfully filled"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "401", description = "User is not authed")
      })
  public ResponseEntity<Void> fillPaymentType(
      @PathVariable(value = "paymentId") Long paymentId,
      @RequestParam(value = "type") String type,
      @RequestParam(value = "amount") Double amount) {
    if (!paymentService.isCurrentUserPaymentCreator(paymentId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    paymentService.updatePaymentType(paymentId, type, amount);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAuthority('FILL_OKTMO')")
  @PostMapping("/{paymentId}/oktmo")
  @Operation(
      description = "Fill the organization OKTMO code",
      responses = {
        @ApiResponse(responseCode = "200", description = "OKTMO code was successfully filled"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "401", description = "User is not authed")
      })
  public ResponseEntity<Void> fillOKTMO(
      @PathVariable(value = "paymentId") Long paymentId,
      @RequestParam(value = "code") String code) {
    if (!paymentService.isCurrentUserPaymentCreator(paymentId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    paymentService.updateOKTMO(paymentId, code);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAuthority('FILL_PAYER_INFO')")
  @PostMapping("/{paymentId}/payer")
  @Operation(
      description = "Fill the payer information",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Payer information was successfully filled"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "401", description = "User is not authed")
      })
  public ResponseEntity<byte[]> updatePayer(
      @PathVariable(value = "paymentId") Long paymentId,
      @RequestParam(value = "name") String name,
      @RequestParam(value = "INN") Long INN,
      @RequestParam(value = "for_self") Boolean forSelf) {

    if (!paymentService.isCurrentUserPaymentCreator(paymentId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    log.debug(
        "updatePayer: paymentId={}, name={}, INN={}, forSelf={}", paymentId, name, INN, forSelf);
    PaymentDocument paymentDocument = paymentService.updatePayer(paymentId, name, INN, forSelf);
    if (paymentDocument != null) {
      byte[] pdf = pdfGenerationService.generatePdf(paymentDocument);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentDispositionFormData("filename", "paymentDocument.pdf");
      return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    } else {
      throw new DocumentGenerationException();
    }
  }

  @PreAuthorize("hasAuthority('FILL_PAYEE_INFO')")
  @PostMapping("/{paymentId}/payee")
  @Operation(
      description = "Fill the organization payee",
      responses = {
        @ApiResponse(responseCode = "200", description = "Payee was successfully filled"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "401", description = "User is not authed")
      })
  public ResponseEntity<byte[]> fillPayee(
      @PathVariable(value = "paymentId") Long paymentId,
      @RequestParam(value = "name") String name,
      @RequestParam(value = "INN") Long INN) {
    if (!paymentService.isCurrentUserPaymentCreator(paymentId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    PaymentDocument paymentDocument = paymentService.updatePayee(paymentId, name, INN);
    if (paymentDocument != null) {
      byte[] pdf = pdfGenerationService.generatePdf(paymentDocument);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentDispositionFormData("filename", "paymentDocument.pdf");
      return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    } else {
      throw new DocumentGenerationException();
    }
  }

  @PreAuthorize("hasAuthority('PROCESS_PAYMENT')")
  @PostMapping("/{paymentId}/process")
  @Operation(
      description = "Process payment",
      responses = {
        @ApiResponse(responseCode = "200", description = "Payment was successfully processed"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "401", description = "User is not authed")
      })
  public ResponseEntity<String> processPayment(
      @PathVariable(value = "paymentId") Long paymentId,
      @RequestParam(value = "payment_method") String paymentMethod) {
    if (!paymentService.isCurrentUserPaymentCreator(paymentId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    boolean paymentProcessed = paymentService.processPayment(paymentId, paymentMethod);
    if (paymentProcessed) {
      return ResponseEntity.ok("Payment processed successfully");
    } else {
      throw new PaymentIsNotProcessedException(paymentId, "");
    }
  }
}

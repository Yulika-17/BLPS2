package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/{paymentId}")
@AllArgsConstructor
@Slf4j
@Tag(name = "Payment type management")
@SecurityRequirement(name = "basicAuth")
public class PaymentTypeController {

  private PaymentService paymentService;

  @PreAuthorize("hasAuthority('FILL_PAYMENT_TYPE_AMOUNT')")
  @PostMapping("/type")
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
}

package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.model.PaymentDocument;
import com.nullnumber1.lab1.service.PaymentService;
import com.nullnumber1.lab1.util.enums.PaymentStatus;
import com.nullnumber1.lab1.util.enums.RoleEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Tag(name = "Admin management")
@SecurityRequirement(name = "basicAuth")
public class AdminController {

  private final PaymentService paymentService;

  @PreAuthorize("hasAuthority('APPROVE_OR_REJECT_PAYMENT')")
  @PostMapping("/reviewPayment/{paymentId}")
  @Operation(
      description = "Review payment",
      responses = {
        @ApiResponse(responseCode = "200", description = "Payment was successfully reviewed"),
        @ApiResponse(responseCode = "400", description = "Payment is not processed yet"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "401", description = "User is not authed"),
        @ApiResponse(
            responseCode = "403",
            description = "User do not have permission for this func")
      })
  public ResponseEntity<?> reviewPayment(
      @PathVariable Long paymentId, @RequestParam Boolean decision) {
    PaymentStatus status = paymentService.reviewPayment(paymentId, decision);
    return ResponseEntity.ok(status);
  }

  @PreAuthorize("hasAuthority('VIEW_PAYMENT_DOCUMENT')")
  @GetMapping("/paymentDocument/{paymentId}")
  @Operation(
      description = "Get payment document",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Payment document was successfully received"),
        @ApiResponse(responseCode = "400", description = "Payment document was not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "401", description = "User is not authed"),
        @ApiResponse(
            responseCode = "403",
            description = "User do not have permission for this func")
      })
  public ResponseEntity<PaymentDocument> getPaymentDocument(@PathVariable Long paymentId) {
    PaymentDocument paymentDocument = paymentService.getPaymentDocument(paymentId);
    return new ResponseEntity<>(paymentDocument, HttpStatus.OK);
  }

  @PreAuthorize("hasAuthority('GIVE_NEW_ROLE')")
  @PostMapping("/newRole")
  @Operation(
      description = "Give new role",
      responses = {
        @ApiResponse(responseCode = "200", description = "Role was successfully added"),
        @ApiResponse(responseCode = "400", description = "User was not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error"),
        @ApiResponse(responseCode = "401", description = "User is not authed"),
        @ApiResponse(
            responseCode = "403",
            description = "User do not have permission for this func")
      })
  public ResponseEntity<?> newRole(@RequestParam String userName) {
    paymentService.addRoleToUser(userName, RoleEnum.ADMIN);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}

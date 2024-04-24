package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.exception.CreatePaymentException;
import com.nullnumber1.lab1.util.enums.PaymentStatus;
import com.nullnumber1.lab1.model.User;
import com.nullnumber1.lab1.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payment management")
@Slf4j
@SecurityRequirement(name = "basicAuth")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasAuthority('CREATE_PAYMENT')")
    @PostMapping("/init")
    @Operation(description = "Create payment", responses = {
            @ApiResponse(responseCode = "200", description = "Payment was successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "User is not authed")
    })

    public ResponseEntity<Long> initPayment() {
        log.info("Received request to create payment for current user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user =  (User) authentication.getPrincipal();
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
    @Operation(description = "Get payment status", responses = {
            @ApiResponse(responseCode = "200", description = "Payment status was successfully received"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "User is not authed")
    })
    public ResponseEntity<PaymentStatus> getPaymentStatus(
            @PathVariable(value = "paymentId") Long paymentId
    ) {
        PaymentStatus status = paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(status);
    }
}

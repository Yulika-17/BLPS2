package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.exception.PaymentIsNotProcessedException;
import com.nullnumber1.lab1.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/{paymentId}")
@Slf4j
@Tag(name = "Payment processing")
@SecurityRequirement(name = "basicAuth")
public class PaymentProcessorController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentProcessorController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    @Operation(description = "Process payment", responses = {
            @ApiResponse(responseCode = "200", description = "Payment was successfully processed"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "User is not authed")
    })
    public ResponseEntity<String> processPayment(
            @PathVariable(value = "paymentId") Long paymentId,
            @RequestParam(value = "payment_method") String paymentMethod) {

        boolean paymentProcessed = paymentService.processPayment(paymentId, paymentMethod);
        if (paymentProcessed) {
            return ResponseEntity.ok("Payment processed successfully");
        } else {
            throw new PaymentIsNotProcessedException(paymentId, "");
        }
    }
}
package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.exception.PaymentIsNotProcessedException;
import com.nullnumber1.lab1.model.PaymentDocument;
import com.nullnumber1.lab1.util.enums.PaymentStatus;
import com.nullnumber1.lab1.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin management")
@SecurityRequirement(name = "basicAuth")
public class AdminController {

    private final PaymentService paymentService;

    @Autowired
    public AdminController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/reviewPayment/{paymentId}")
    @Operation(description = "Review payment", responses = {
            @ApiResponse(responseCode = "200", description = "Payment was successfully reviewed"),
            @ApiResponse(responseCode = "400", description = "Payment is not processed yet"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "User is not authed"),
            @ApiResponse(responseCode = "403", description = "User do not have permission for this func")
    })
    public ResponseEntity<?> reviewPayment(@PathVariable Long paymentId, @RequestParam Boolean decision) {
        PaymentStatus status = paymentService.reviewPayment(paymentId, decision);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/paymentDocument/{paymentId}")
    @Operation(description = "Get payment document", responses = {
            @ApiResponse(responseCode = "200", description = "Payment document was successfully received"),
            @ApiResponse(responseCode = "400", description = "Payment document was not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "User is not authed"),
            @ApiResponse(responseCode = "403", description = "User do not have permission for this func")
    })
    public ResponseEntity<PaymentDocument> getPaymentDocument(@PathVariable Long paymentId) {
        PaymentDocument paymentDocument = paymentService.getPaymentDocument(paymentId);
        return new ResponseEntity<>(paymentDocument, HttpStatus.OK);
    }
}

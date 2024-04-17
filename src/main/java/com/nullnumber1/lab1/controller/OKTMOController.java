package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/{paymentId}")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "basicAuth")
@Tag(name = "OKTMO management")
public class OKTMOController {

    private PaymentService paymentService;

    @Autowired
    public OKTMOController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/oktmo")
    @Operation(description = "Fill the organization OKTMO code", responses = {
            @ApiResponse(responseCode = "200", description = "OKTMO code was successfully filled"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "User is not authed")
    })
    public ResponseEntity<Void> fillOKTMO(
            @PathVariable(value = "paymentId") Long paymentId,
            @RequestParam(value = "code") String code
    ) {
        paymentService.updateOKTMO(paymentId, code);
        return ResponseEntity.ok().build();
    }
}

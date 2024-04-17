package com.nullnumber1.lab1.controller;

import com.nullnumber1.lab1.exception.DocumentGenerationException;
import com.nullnumber1.lab1.model.PaymentDocument;
import com.nullnumber1.lab1.service.PaymentService;
import com.nullnumber1.lab1.service.PdfGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/{paymentId}")
@Slf4j
@Tag(name = "Payer management")
@SecurityRequirement(name = "basicAuth")
public class PayerController {

    private final PaymentService paymentService;
    private final PdfGenerationService pdfGenerationService;

    @Autowired
    public PayerController(PaymentService paymentService, PdfGenerationService pdfGenerationService) {
        this.paymentService = paymentService;
        this.pdfGenerationService = pdfGenerationService;
    }

    @PostMapping("/payer")
    @Operation(description = "Fill the payer information", responses = {
            @ApiResponse(responseCode = "200", description = "Payer information was successfully filled"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "User is not authed")
    })
    public ResponseEntity<byte[]> updatePayer(
            @PathVariable(value = "paymentId") Long paymentId,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "INN") Long INN,
            @RequestParam(value = "for_self") Boolean forSelf) {


        log.debug("updatePayer: paymentId={}, name={}, INN={}, forSelf={}", paymentId, name, INN, forSelf);
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
}

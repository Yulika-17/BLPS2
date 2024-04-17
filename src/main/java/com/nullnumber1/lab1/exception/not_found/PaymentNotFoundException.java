package com.nullnumber1.lab1.exception.not_found;

public class PaymentNotFoundException extends ResourceNotFoundException {
    public PaymentNotFoundException(Long paymentId) {
        super("payment", "id", String.valueOf(paymentId));
    }
}

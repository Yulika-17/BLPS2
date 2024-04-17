package com.nullnumber1.lab1.exception;

public class PaymentIsNotProcessedException extends RuntimeException {
    public PaymentIsNotProcessedException(Long paymentId, String status) {
        super("Payment [" + paymentId + "] is not processed yet, status [" + status + "]");
    }
}

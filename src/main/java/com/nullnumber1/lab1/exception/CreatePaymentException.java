package com.nullnumber1.lab1.exception;

public class CreatePaymentException extends RuntimeException {
  public CreatePaymentException() {
    super("Payment cannot be created");
  }
}

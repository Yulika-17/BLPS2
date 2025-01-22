package com.nullnumber1.lab1.exception.not_found;

public class InnDoesntExistException extends ResourceNotFoundException {
  public InnDoesntExistException(Long paymentId) {
    super("inn", "id", String.valueOf(paymentId));
  }
}

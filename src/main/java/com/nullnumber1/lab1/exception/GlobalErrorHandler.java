package com.nullnumber1.lab1.exception;

import com.nullnumber1.lab1.dto.Error;
import com.nullnumber1.lab1.exception.not_found.ResourceNotFoundException;
import com.nullnumber1.lab1.util.enums.Code;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@ResponseBody
public class GlobalErrorHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public Error handleNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    Error errorDTO =
        new Error(
            Code.RESOURCE_NOT_FOUND, new Date(), ex.getMessage(), request.getDescription(false));
    return errorDTO;
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public Error handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
    Error errorDTO =
        new Error(Code.AUTH_ERROR, new Date(), ex.getMessage(), request.getDescription(false));
    return errorDTO;
  }

  @ExceptionHandler(PaymentIsNotProcessedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public Error handlePaymentIsNotProcessedException(
      PaymentIsNotProcessedException ex, WebRequest request) {
    Error errorDTO =
        new Error(Code.BAD_REQUEST, new Date(), ex.getMessage(), request.getDescription(false));
    return errorDTO;
  }

  @ExceptionHandler(CreatePaymentException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public Error handleCreatePaymentException(CreatePaymentException ex, WebRequest request) {
    Error errorDTO =
        new Error(Code.BAD_REQUEST, new Date(), ex.getMessage(), request.getDescription(false));
    return errorDTO;
  }

  @ExceptionHandler(DocumentGenerationException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public Error handleDocumentGenerationException(
      DocumentGenerationException ex, WebRequest request) {
    Error errorDTO =
        new Error(Code.DOCUMENT_ERROR, new Date(), ex.getMessage(), request.getDescription(false));
    return errorDTO;
  }

  @ExceptionHandler(SaveDocumentException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public Error handleSaveDocumentException(SaveDocumentException ex, WebRequest request) {
    Error errorDTO =
        new Error(Code.DOCUMENT_ERROR, new Date(), ex.getMessage(), request.getDescription(false));
    return errorDTO;
  }
}

package com.nullnumber1.lab1.exception;

public class DocumentGenerationException extends RuntimeException {
  public DocumentGenerationException() {
    super("Error occurred while generating PDF");
  }
}

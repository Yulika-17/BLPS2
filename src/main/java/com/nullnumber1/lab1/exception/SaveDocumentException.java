package com.nullnumber1.lab1.exception;

public class SaveDocumentException extends RuntimeException {
    public SaveDocumentException() {
        super("Error occurred while saving PDF to a file");
    }
}

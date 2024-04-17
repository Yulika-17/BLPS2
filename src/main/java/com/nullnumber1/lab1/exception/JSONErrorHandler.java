package com.nullnumber1.lab1.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.nullnumber1.lab1.dto.Error;
import com.nullnumber1.lab1.util.enums.Code;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
@ResponseBody
public class JSONErrorHandler {

    @ExceptionHandler(UnrecognizedPropertyException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error handleUnrecognizedPropertyException(UnrecognizedPropertyException ex, WebRequest request) {
        Error error = new Error(
                Code.JSON_ERROR, new Date(), "Неидентифицированное поле в JSON.", request.getDescription(false));
        return error;
    }

    @ExceptionHandler({JsonParseException.class, JsonMappingException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Error handleJsonException(Exception ex, WebRequest request) {
        Error error = new Error(
                Code.JSON_ERROR, new Date(), ex.getMessage(), request.getDescription(false));
        return error;
    }
}

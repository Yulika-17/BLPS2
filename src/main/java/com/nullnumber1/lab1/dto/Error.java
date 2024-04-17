package com.nullnumber1.lab1.dto;

import com.nullnumber1.lab1.util.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Error {
    private Code code;
    private Date timestamp;
    private String message;
    private String description;
}
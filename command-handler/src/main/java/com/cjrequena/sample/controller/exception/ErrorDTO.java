package com.cjrequena.sample.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    
    private String timestamp;
    private int status;
    private String errorCode;
    private String message;
    private List<ValidationError> validationErrors;
}

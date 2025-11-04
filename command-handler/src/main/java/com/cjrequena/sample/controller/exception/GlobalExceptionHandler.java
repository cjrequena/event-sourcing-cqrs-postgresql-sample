package com.cjrequena.sample.controller.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cjrequena.sample.shared.common.Constant.ISO_LOCAL_DATE_TIME;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler({ControllerException.class})
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleControllerException(ControllerException ex) {
    log.info("Exception: {}", ex.getMessage());
    return buildErrorResponse(ex.getHttpStatus(), ex.getMessage());
  }

  @ExceptionHandler({ControllerRuntimeException.class})
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleControllerRuntimeException(ControllerRuntimeException ex) {
    log.info("Exception: {}", ex.getMessage());
    return buildErrorResponse(ex.getHttpStatus(), ex.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    log.info("Exception: {}", ex.getMessage());

    Throwable cause = ex.getCause();
    String message;

    if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException invalidFormatEx) {
      Class<?> targetType = invalidFormatEx.getTargetType();

      // Handle invalid Date/Time format
      if (targetType != null && java.time.temporal.Temporal.class.isAssignableFrom(targetType)) {
        String field = invalidFormatEx.getPath().stream()
          .map(JsonMappingException.Reference::getFieldName)
          .filter(Objects::nonNull)
          .reduce((a, b) -> a + "." + b)
          .orElse("unknown");

        String invalidValue = String.valueOf(invalidFormatEx.getValue());
        message = String.format("Invalid date/time format for field '%s': '%s . Expected format: '%s' ", field, invalidValue, ISO_LOCAL_DATE_TIME);

        return buildErrorResponse(HttpStatus.BAD_REQUEST ,message);
      }

      // Handle invalid Enum values
      if (targetType != null && targetType.isEnum()) {
        String field = invalidFormatEx.getPath().stream()
          .map(JsonMappingException.Reference::getFieldName)
          .filter(Objects::nonNull)
          .reduce((a, b) -> a + "." + b)
          .orElse("unknown");

        String invalidValue = String.valueOf(invalidFormatEx.getValue());
        String acceptedValues = Arrays.stream(targetType.getEnumConstants())
          .map(Object::toString)
          .collect(Collectors.joining(", "));

        message = String.format("Invalid value for enum field '%s': '%s'. Accepted values are: [%s].", field, invalidValue, acceptedValues);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
      }
    }

    // Generic fallback for malformed JSON or other unreadable content
    message = "Malformed JSON request or invalid request body.";
    return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException ex) {
    log.error("Validation error: {}", ex.getMessage());
    List<ValidationError> validationErrors = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(this::mapToValidationError)
      .collect(Collectors.toList());
    return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed for one or more fields", validationErrors);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
    log.error("Illegal argument: {}", ex.getMessage());
    return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorDTO> handleIllegalStateException(IllegalStateException ex) {
    log.error("Illegal state: {}", ex.getMessage());
    return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDTO> handleGenericException(Exception ex) {
    log.error("Unexpected error occurred", ex);
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");

  }

  private ResponseEntity<ErrorDTO> buildErrorResponse(HttpStatus status, String message) {
    ErrorDTO error = ErrorDTO.builder()
      .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
      .status(status.value())
      .errorCode(status.getReasonPhrase())
      .message(message)
      .build();
    return ResponseEntity.status(status).body(error);
  }

  private ResponseEntity<ErrorDTO> buildErrorResponse(HttpStatus status, String message, List<ValidationError> validationErrors) {
    ErrorDTO error = ErrorDTO.builder()
      .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
      .status(status.value())
      .errorCode(status.getReasonPhrase())
      .message(message)
      .validationErrors(validationErrors)
      .build();
    return ResponseEntity.status(status).body(error);
  }

  private ValidationError mapToValidationError(FieldError fieldError) {
    return ValidationError.builder()
      .field(fieldError.getField())
      .message(fieldError.getDefaultMessage())
      .rejectedValue(fieldError.getRejectedValue())
      .build();
  }
}

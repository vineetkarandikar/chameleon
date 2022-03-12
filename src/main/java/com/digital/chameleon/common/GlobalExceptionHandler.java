package com.digital.chameleon.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(value = DigitalChameleonApplicationException.class)
  public ResponseEntity<?> DigitalOnboardingException(
      DigitalChameleonApplicationException digitalChameleonApplicationException) {
    ApiResponse<String> apiError = new ApiResponse<String>();
    apiError.setErrorCode(digitalChameleonApplicationException.getStatus().toString());
    apiError.setMessage(digitalChameleonApplicationException.getMessage());
    apiError.setSuccess(false);
    logger.error("Exception occured due to  " + digitalChameleonApplicationException.getMessage());
    return new ResponseEntity<>(apiError, digitalChameleonApplicationException.getStatus());

  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<?> databaseConnectionFailsException(Exception exception) {
    ApiResponse<String> apiError = new ApiResponse<String>();
    apiError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
    apiError.setSuccess(false);
    apiError.setMessage(exception.getMessage());
    logger.error("Exception occured due to  " + exception.getMessage());
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}

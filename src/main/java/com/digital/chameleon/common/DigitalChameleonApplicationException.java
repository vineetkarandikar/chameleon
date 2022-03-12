package com.digital.chameleon.common;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DigitalChameleonApplicationException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private HttpStatus status;
  private String message;

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

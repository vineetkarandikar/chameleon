package com.digital.chameleon.common;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
  private HttpStatus status;
  private LocalDateTime timestamp;
  private String message;
  private String debugMessage;
}

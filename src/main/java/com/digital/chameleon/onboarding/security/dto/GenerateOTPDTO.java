package com.digital.chameleon.onboarding.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GenerateOTPDTO {
  private String emailId;
}

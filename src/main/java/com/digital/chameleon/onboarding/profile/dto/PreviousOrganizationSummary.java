package com.digital.chameleon.onboarding.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreviousOrganizationSummary {
  private String orgName;
  private String designation;
  private Integer tenureInMonths;
}

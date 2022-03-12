package com.digital.chameleon.onboarding.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpDTO {
	private String mobile;
	private String emailId;
	private String panNumber;
	private String firstName;
	private String lastName;
}

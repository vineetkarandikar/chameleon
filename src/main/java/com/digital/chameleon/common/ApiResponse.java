package com.digital.chameleon.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
	private T response;
	private boolean success=true;
	private String message;
	private String errorCode;
}

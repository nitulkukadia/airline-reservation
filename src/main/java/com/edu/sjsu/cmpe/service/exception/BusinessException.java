package com.edu.sjsu.cmpe.service.exception;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 31413849210756292L;
	private String errorCode;
	
	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
}

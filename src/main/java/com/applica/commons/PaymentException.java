package com.applica.commons;

public class PaymentException extends RuntimeException {

	private Integer code;

	public PaymentException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	private static final long serialVersionUID = -174997289890954219L;

}

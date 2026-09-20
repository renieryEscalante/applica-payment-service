package com.applica.dto;

import java.io.Serializable;

import lombok.Data;

 @Data
public class SavePaymentResponse implements Serializable {

	private String code;
	private String message;
	private static final long serialVersionUID = 1270204439742757095L;
}

package com.applica.service;

import com.applica.dto.PaymentDto;
import com.applica.dto.Response;

public interface PaymentService {
	public Response makePayment(PaymentDto payment);
}

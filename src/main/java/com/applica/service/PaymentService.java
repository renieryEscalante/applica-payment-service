package com.applica.service;

import java.util.List;

import com.applica.dto.PaymentDto;
import com.applica.dto.Response;
import com.applica.model.Payment;

public interface PaymentService {
	public Response makePayment(PaymentDto payment);
	public List<Payment> getPayments();
}

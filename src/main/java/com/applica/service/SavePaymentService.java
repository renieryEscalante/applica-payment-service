package com.applica.service;

import com.applica.dto.PaymentDto;
import com.applica.dto.SavePaymentResponse;

public interface SavePaymentService {

	public SavePaymentResponse savePayment(PaymentDto paymentDto);
}

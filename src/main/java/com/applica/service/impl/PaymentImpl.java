package com.applica.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.applica.commons.PaymentException;
import com.applica.dto.PaymentDto;
import com.applica.dto.Response;
import com.applica.dto.SavePaymentResponse;
import com.applica.model.Payment;
import com.applica.repository.PaymentRepository;
import com.applica.service.PaymentService;
import com.applica.service.SavePaymentService;

@Service
public class PaymentImpl implements PaymentService {
	
	private static final Logger LOGG = LoggerFactory.getLogger(PaymentImpl.class);

	
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private SavePaymentService savePaymentService;

	@Override
	@Transactional
	public Response makePayment(PaymentDto paymentDto) {
		Response response = null;
		Payment payment = null;
		SavePaymentResponse savePaymentResponse = null;
		
		try {
			validatePayment(paymentDto);
			payment = fillPayment(paymentDto);
			payment = paymentRepository.save(payment);
			
			savePaymentResponse = savePaymentService.savePayment(paymentDto);			
			
		} catch (PaymentException e) {
			throw e;
		} catch (Exception e) {
			LOGG.error("Ocurrió una excepción mientras se intentaba realizar el pago: ", e);
			throw new PaymentException(e.getMessage(), HttpStatus.CONFLICT.value());
		}
		return response;
	}
	
	private Payment fillPayment(PaymentDto paymentDto) {
		Payment payment = new Payment();
		payment.setTransactionId(paymentDto.getId());
		payment.setCardNumber(maskCardNumber(paymentDto.getCardNumber()));
		payment.setCardName(paymentDto.getCardName());
		payment.setCustomerId(paymentDto.getCustomerId());
		payment.setAmount(paymentDto.getAmount());
		payment.setCurrency(paymentDto.getCurrency());
		payment.setTransactionDate(paymentDto.getTimestamp());
		return payment;
	}
	
	public static String maskCardNumber(String cardNumber) {
	    if (cardNumber == null || !cardNumber.matches("\\d{13,19}")) {
	        throw new PaymentException("Número de tarjeta inválido", HttpStatus.CONFLICT.value());
	    }

	    return "X".repeat(cardNumber.length() - 4)
	            + cardNumber.substring(cardNumber.length() - 4);
	}

	public void validatePayment(PaymentDto payment) {

		if (payment == null) {
			throw new PaymentException("El payment no puede ser null", HttpStatus.BAD_REQUEST.value());
		}

		if (payment.getId() == null) {
			throw new PaymentException("El id es requerido", HttpStatus.BAD_REQUEST.value());
		}

		if (payment.getCardNumber() == null || payment.getCardNumber().trim().isEmpty()) {
			throw new PaymentException("El cardNumber es requerido", HttpStatus.BAD_REQUEST.value());
		}

		if (payment.getCvv() == null || payment.getCvv().trim().isEmpty()) {
			throw new PaymentException("El cvv es requerido", HttpStatus.BAD_REQUEST.value());
		}

		if (payment.getDueDate() == null || payment.getDueDate().trim().isEmpty()) {
			throw new PaymentException("El dueDate es requerido", HttpStatus.BAD_REQUEST.value());
		}

		if (payment.getCardName() == null || payment.getCardName().trim().isEmpty()) {
			throw new PaymentException("El cardName es requerido", HttpStatus.BAD_REQUEST.value());
		}

		if (payment.getCustomerId() == null) {
			throw new PaymentException("El customerId es requerido", HttpStatus.BAD_REQUEST.value());
		}

		if (payment.getAmount() == null) {
			throw new PaymentException("El amount es requerido", HttpStatus.BAD_REQUEST.value());
		}

		if (payment.getCurrency() == null || payment.getCurrency().trim().isEmpty()) {
			throw new PaymentException("El currency es requerido", HttpStatus.BAD_REQUEST.value());
		}

		if (payment.getTimestamp() == null) {
			throw new PaymentException("El timestamp es requerido", HttpStatus.BAD_REQUEST.value());
		}
	}

}

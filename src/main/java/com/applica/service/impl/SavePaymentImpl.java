package com.applica.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.applica.client.SavePaymentClient;
import com.applica.commons.PaymentException;
import com.applica.dto.PaymentDto;
import com.applica.dto.SavePaymentResponse;
import com.applica.service.SavePaymentService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Service
public class SavePaymentImpl implements SavePaymentService {
	
	private static final Logger LOGG = LoggerFactory.getLogger(SavePaymentImpl.class);

	
	@Autowired
	private SavePaymentClient savePaymentClient;

	@Override
	public SavePaymentResponse savePayment(PaymentDto paymentDto) {
		SavePaymentResponse savePaymentResponse = null;
		try {
			
			JavaTimeModule module = new JavaTimeModule();

			module.addSerializer(
			    LocalDateTime.class,
			    new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
			);
			
			XmlMapper xmlMapper = new XmlMapper();
			xmlMapper.registerModule(module);

			String xml = xmlMapper.writeValueAsString(paymentDto);
			
			// Simula la petición al proveedor de pagos
			savePaymentResponse = savePaymentClient.savePayment(xml);
			
		} catch (PaymentException e) {
			throw e;
		} catch (Exception e) {
			LOGG.error("Ocurrió una excepción mientras se intentaba guardar el pago: ", e);
			throw new PaymentException(e.getMessage(), HttpStatus.CONFLICT.value());
		}
		return savePaymentResponse;
	}

	
	
}

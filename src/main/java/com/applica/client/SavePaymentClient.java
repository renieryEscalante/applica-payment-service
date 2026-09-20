package com.applica.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.applica.commons.PaymentException;
import com.applica.dto.SavePaymentResponse;

@Component
public class SavePaymentClient {

	private static final Logger LOGG = LoggerFactory.getLogger(SavePaymentClient.class);

	public SavePaymentResponse savePayment(String body) {
		SavePaymentResponse savePaymentResponse = null;
		
		LOGG.info(body);
		saveOnDisc(body);
		
		savePaymentResponse = new SavePaymentResponse();
		savePaymentResponse.setCode(HttpStatus.OK.toString());
		savePaymentResponse.setMessage(null);
		
		return savePaymentResponse;		
	}
	
	private void saveOnDisc(String body) {
		String fecha = null;
		String fileName = null;

		try {
			
			fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
			fileName = "payment_" + fecha + ".xml";
			Path path = Paths.get("src/main/resources/xml", fileName);
			
			Files.createDirectories(path.getParent());
			Files.writeString(path, body, StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			LOGG.error("Ocurrió una excepción mientras se intentaba guardar en disco el pago: ", e);
			throw new PaymentException(e.getMessage(), HttpStatus.CONFLICT.value());
		}
	}

}

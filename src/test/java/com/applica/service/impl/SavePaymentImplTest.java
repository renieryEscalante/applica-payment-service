package com.applica.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.applica.client.SavePaymentClient;
import com.applica.dto.PaymentDto;
import com.applica.dto.SavePaymentResponse;

@ExtendWith(MockitoExtension.class)
class SavePaymentImplTest {

	@Mock
	private SavePaymentClient savePaymentClient;

	@InjectMocks
	private SavePaymentImpl savePaymentImpl;

	@Test
	void savePaymentSerializesPaymentAndReturnsClientResponse() {
		PaymentDto payment = new PaymentDto();
		payment.setId(10L);
		payment.setAmount(new BigDecimal("25.50"));
		payment.setCurrency("USD");
		payment.setTimestamp(LocalDateTime.of(2026, 9, 20, 10, 30));

		SavePaymentResponse expectedResponse = new SavePaymentResponse();
		expectedResponse.setCode(HttpStatus.OK.toString());
		when(savePaymentClient.savePayment(anyString())).thenReturn(expectedResponse);

		SavePaymentResponse response = savePaymentImpl.savePayment(payment);

		ArgumentCaptor<String> xmlCaptor = ArgumentCaptor.forClass(String.class);
		verify(savePaymentClient).savePayment(xmlCaptor.capture());
		assertSame(expectedResponse, response);
		assertTrue(xmlCaptor.getValue().contains("<id>10</id>"));
		assertTrue(xmlCaptor.getValue().contains("<amount>25.50</amount>"));
		assertTrue(xmlCaptor.getValue().contains("<timestamp>2026-09-20T10:30:00</timestamp>"));
	}
}

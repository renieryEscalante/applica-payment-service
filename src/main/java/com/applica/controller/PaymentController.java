package com.applica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.applica.dto.PaymentDto;
import com.applica.dto.Response;
import com.applica.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;

	@Operation(
		    summary = "Procesar un pago",
		    description = "Recibe la información de una tarjeta y procesa el pago."
		)
		@ApiResponses({
		    @ApiResponse(
		        responseCode = "200",
		        description = "Pago procesado correctamente"
		    ),
		    @ApiResponse(
		        responseCode = "400",
		        description = "Datos de entrada inválidos"
		    ),
		    @ApiResponse(
		        responseCode = "401",
		        description = "No autorizado"
		    )
		})
	@PostMapping("/payments")
	public ResponseEntity<Response> makePayment(@RequestBody PaymentDto payment){
		Response response;
		response = paymentService.makePayment(payment);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
}

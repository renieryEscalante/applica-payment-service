package com.applica.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PaymentDto implements Serializable {

	private Long id;
	private String cardNumber;
	private String cvv;
	private String dueDate;
	private String cardName;
	private Long customerId;
	private BigDecimal amount;
	private String currency;
	private LocalDateTime timestamp;
	
	private static final long serialVersionUID = 6851889615954318376L;

}

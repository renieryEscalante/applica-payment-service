package com.applica.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pagos")
public class Payment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "IDTRANSACION")
	private Long transactionId;
	@Column(name = "NUMEROTARJETA")
	private String cardNumber;
	@Column(name = "NOMBRETARJETA")
	private String cardName;
	@Column(name = "IDCLIENTE")
	private Long customerId;
	@Column(name = "MONTO")
	private BigDecimal amount;
	@Column(name = "MONEDA")
	private String currency;
	@Column(name = "FECHTRANSACCION")
	private LocalDateTime transactionDate;
	@Column(name = "FECHAMODIFICACION")
	private LocalDateTime modificationDate;
	@Column(name = "FECHAINSERCION")
	private LocalDateTime insertionDate;
	
    @PrePersist
    public void prePersist() {
        LocalDateTime today = LocalDateTime.now();
        this.insertionDate = today;
        this.modificationDate = today;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.insertionDate = LocalDateTime.now();
    }

	private static final long serialVersionUID = -982658456070007049L;

}

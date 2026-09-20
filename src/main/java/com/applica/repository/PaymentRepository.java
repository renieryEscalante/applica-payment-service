package com.applica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.applica.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	
	
}

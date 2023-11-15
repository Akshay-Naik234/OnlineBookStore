package com.springboot.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.bookStore.entity.OrderPaymentDetails;

public interface OrderPaymentRepository extends JpaRepository<OrderPaymentDetails, Long> {
	
	@Query("SELECT o FROM OrderPaymentDetails o WHERE o.PaymentorderId = :PaymentorderId")
	public OrderPaymentDetails findByPaymentorderId(String PaymentorderId);

}

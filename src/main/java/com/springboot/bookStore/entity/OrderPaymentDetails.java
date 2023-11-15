package com.springboot.bookStore.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderPaymentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long myOrderid;

	private String PaymentorderId;

	private String amount;

	private String receipt;

	private String status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String paymentId;

}

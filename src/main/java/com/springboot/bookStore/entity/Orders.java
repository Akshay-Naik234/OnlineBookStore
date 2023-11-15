package com.springboot.bookStore.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "order_time", nullable = false, updatable = false)
	@CreationTimestamp
	private Timestamp order_time;

	private float total;

	private String order_status;

	private String first_name;

	private String phone;

	private String email;

	private String address;

	private String city;

	private String state;

	private long zipcode;

	private String country;


}

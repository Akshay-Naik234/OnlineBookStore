package com.springboot.bookStore.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import javax.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String address;

	private String city;

	private String state;

	private long zipcode;

	private String country;
	
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", nullable = false)
	private User user;


	@Override
	public String toString() {
		return "Address [id=" + id + ", address=" + address + ", city=" + city + ", state=" + state + ", zipcode="
				+ zipcode + ", country=" + country + ", user=" + user + "]";
	}

	

}

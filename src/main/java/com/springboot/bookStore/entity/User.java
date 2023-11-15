package com.springboot.bookStore.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotBlank(message="Name field is required !!")
	@Size(min=2,max=20,message="Min 2 and Max 20 characters are allowed !!")
	private String name;

	@Column(unique = true)
	@Email(regexp="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
	private String email;
	
	@NotBlank(message="Phone field is required !!")
	@Size(min=10,message="Phone number must be 10 character")
	private String phone;
	
	@NotBlank(message="Country is required !!")
	private String Country;
	
	@NotBlank(message="Password is required !!")
	@Size(min=3,message="atleast 3 characters required")
	private String password;
	
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String image;
	
	private String role;
	
	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
	private Address address;
	
	public boolean hasRole(String roleName) {
		if(roleName.equals("ROLE_USER")) {
			return true;
		}
		else if(roleName.equals("ROLE_ADMIN")) {
			return true;
		}
		return false;
	}
	
}

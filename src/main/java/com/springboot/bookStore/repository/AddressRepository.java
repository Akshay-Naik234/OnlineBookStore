package com.springboot.bookStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.bookStore.entity.Address;
import com.springboot.bookStore.entity.User;

public interface AddressRepository extends JpaRepository<Address, Long> {
	
	public Address findByUser(User user);

}

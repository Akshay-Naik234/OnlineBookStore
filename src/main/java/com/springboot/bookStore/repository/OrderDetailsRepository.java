package com.springboot.bookStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.bookStore.entity.Order_details;

public interface OrderDetailsRepository extends JpaRepository<Order_details, Long> {

	public List<Order_details> findByOrdersId(long id);
}

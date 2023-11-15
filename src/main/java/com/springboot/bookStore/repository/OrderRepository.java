package com.springboot.bookStore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.bookStore.entity.Orders;
import com.springboot.bookStore.entity.User;

public interface OrderRepository extends JpaRepository<Orders, Long> {
	
	@Query("SELECT o FROM Orders o WHERE o.order_status = :s")
	public Page<Orders> findByOrder_status(String s,Pageable pageable);
	
	public List<Orders> findByUser(User user);
	
	public Page<Orders> findAll(Pageable pageable);
	
}

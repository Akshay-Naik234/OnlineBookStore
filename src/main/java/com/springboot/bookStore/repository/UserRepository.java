package com.springboot.bookStore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.bookStore.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);
	
	@Query("select u from User u where u.email = :email")
	public User getUserByUserName(@Param("email") String email);
}

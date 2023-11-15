package com.springboot.bookStore.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.repository.UserRepository;


public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User existingUser = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with the email " +email));
		
		return new org.springframework.security.core.userdetails.User(existingUser.getEmail(), existingUser.getPassword(), new ArrayList<>());
	}
}

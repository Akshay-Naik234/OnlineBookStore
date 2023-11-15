package com.springboot.bookStore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bookStore.entity.Book;
import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.repository.BookRepository;
import com.springboot.bookStore.repository.UserRepository;

@RestController
public class SearchController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("/search/{query}")
	public ResponseEntity<List<Book>> search(@PathVariable("query") String query) {
		List<Book> books = bookRepository.findByNameContaining(query);
		return ResponseEntity.ok(books);
	}
}
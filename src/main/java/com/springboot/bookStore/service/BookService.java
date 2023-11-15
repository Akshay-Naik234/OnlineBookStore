package com.springboot.bookStore.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.bookStore.entity.Book;

public interface BookService {
	
	Page<Book> getAllBooks(Pageable page);
	
	Book getBookById(long id);
	
	void deleteById(long id);
}

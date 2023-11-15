package com.springboot.bookStore.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.bookStore.entity.Book;
import com.springboot.bookStore.exception.ResourceNotFoundException;
import com.springboot.bookStore.repository.BookRepository;
import com.springboot.bookStore.service.BookService;

@Service
public class BookServiceImpl implements BookService{
	
	@Autowired
	private BookRepository bookRepository;


	@Override
	public Book getBookById(long id) {
		Book book = bookRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Book", "id", id));
		return book;
	}

	@Override
	public void deleteById(long id) {
		Book book = bookRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Book", "id", id));
		bookRepository.deleteById(id);
	}

	@Override
	public Page<Book> getAllBooks(Pageable page) {
		return bookRepository.findAll(page);
	}

}

package com.springboot.bookStore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.bookStore.entity.Book;
import com.springboot.bookStore.entity.User;

public interface BookRepository extends JpaRepository<Book, Long> {
	Page<Book> findAll(Pageable page);
	
	@Query("SELECT b FROM Book b WHERE b.Category = :category")
	Page<Book> findByCategory(Pageable page,String category);
	
	@Query("SELECT b.Category FROM Book AS b GROUP BY b.Category")
	List<String> findByCategoryGroup();
	
	public List<Book> findByNameContaining(String keywords);
	
}

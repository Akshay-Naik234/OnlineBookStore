package com.springboot.bookStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.bookStore.entity.Book;
import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.entity.cart_items;

@Repository
public interface CartItemRepository extends JpaRepository<cart_items, Long> {
	
	public List<cart_items> findByUser(User user);
	
	public cart_items findByUserAndBook(User user,Book book);

	@Query("UPDATE cart_items c SET c.quantity= ?1 WHERE c.book.id = ?2 " + "AND c.user.id = ?3")
	@Modifying
	public void updateQuantity(int quantity,long bookId,long userId);

	@Query("DELETE FROM cart_items c WHERE c.user.id = ?1 AND c.book.id =?2")
	@Modifying
	public void deleteByUserAndBook(long userId,long bookId);
	
	public void deleteByUser(User user);
}

package com.springboot.bookStore;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.springboot.bookStore.entity.Book;
import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.entity.cart_items;
import com.springboot.bookStore.repository.CartItemRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
class ShoppingCartTest {

	@Autowired
	private CartItemRepository cartRepo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	
	@Test
	public void testAddOneCartItem() {
		Book book = entityManager.find(Book.class,22l);
		User user = entityManager.find(User.class,9l);
		
		cart_items newItem = new cart_items();
		newItem.setUser(user);
		newItem.setBook(book);
		newItem.setQuantity(2);
		System.out.println("hi");
		cart_items cart_items = cartRepo.save(newItem);
		assertTrue(cart_items.getId()>0);
	}
	
/*
	@Test
	public void testGetCartItemsByUser() {
		User user = new User();
		user.setId(9);
		List<cart_items> cartItems = cartRepo.findByUser(user);
		assertEquals(2, cartItems.size());	
	}
	*/

}

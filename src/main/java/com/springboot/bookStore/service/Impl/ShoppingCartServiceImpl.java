package com.springboot.bookStore.service.Impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.bookStore.entity.Book;
import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.entity.cart_items;
import com.springboot.bookStore.repository.BookRepository;
import com.springboot.bookStore.repository.CartItemRepository;
import com.springboot.bookStore.service.ShoppingCartService;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private BookRepository bookRepo;

	@Autowired
	private CartItemRepository cartRepo;

	@Override
	public List<cart_items> listCartItems(User user) {
		return cartRepo.findByUser(user);
	}

	@Override
	public int addProduct(long bookId, int quantity, User user) {
		int addedQuantity = quantity;
		Book book = bookRepo.findById(bookId).get();

		cart_items cartItem = cartRepo.findByUserAndBook(user, book);
		if(cartItem!=null) {
			addedQuantity=cartItem.getQuantity() + quantity;
			if(addedQuantity>book.getAvailableQuantity()) {
				return 0;
			}
			cartItem.setQuantity(addedQuantity);
		}
		else {
			cartItem = new cart_items();
			cartItem.setQuantity(quantity);
			cartItem.setUser(user);
			cartItem.setBook(book);
		}
		
		cartRepo.save(cartItem);
		return addedQuantity;
	}

	@Override
	public float updateQuantity(long bookId, int quantity, User user) {
		
		cartRepo.updateQuantity(quantity, bookId, user.getId());
		Book book = bookRepo.findById(bookId).get();
		float subtotal = (float) (book.getPrice()*quantity);
		return subtotal;
	}

	@Override
	public void removeBook(long bookId, User user) {
		cartRepo.deleteByUserAndBook(user.getId(), bookId);
	}

}

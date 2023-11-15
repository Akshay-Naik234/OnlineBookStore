package com.springboot.bookStore.service;

import java.util.List;

import javax.transaction.Transactional;

import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.entity.cart_items;

public interface ShoppingCartService {
	
	public List<cart_items> listCartItems(User user);
	
	public int addProduct(long bookId,int quantity,User user);
	
	public float updateQuantity(long bookId,int quantity,User user);

	public void removeBook(long bookId,User user);
	
}

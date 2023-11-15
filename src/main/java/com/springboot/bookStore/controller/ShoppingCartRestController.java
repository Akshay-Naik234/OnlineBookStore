package com.springboot.bookStore.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.repository.UserRepository;
import com.springboot.bookStore.service.ShoppingCartService;

@RestController
public class ShoppingCartRestController {

	@Autowired
	private ShoppingCartService cartService;

	@Autowired
	private UserRepository userRepo;

	@PostMapping("/cart/add/{pid}/{qty}")
	public String addProductToCart(@PathVariable("pid") int bookId, @PathVariable("qty") int quantity,
			Principal principal) {
		if (principal.getName() == null) {
			return "You Must Login to add this Product to shopping cart.";
		}

		String name = principal.getName();
		User user = userRepo.findByEmail(name).get();
		int addedQuantity = cartService.addProduct(bookId, quantity, user);
		if (addedQuantity == 0) {
			return "We can not add the Product Due to less stock";
		}

		return addedQuantity + "items of this product were added to shopping cart";
	}

	@PostMapping("/cart/update/{pid}/{qty}")
	public String updateQuantity(@PathVariable("pid") int bookId, @PathVariable("qty") int quantity,
			Principal principal) {
		if (principal.getName() == null) {
			return "You Must Login to add this Product to shopping cart.";
		}

		String name = principal.getName();
		User user = userRepo.findByEmail(name).get();
		float subtotal = cartService.updateQuantity(bookId, quantity, user);

		return String.valueOf(subtotal);

	}

	@PostMapping("/cart/remove/{pid}")
	public String removeCart(@PathVariable("pid") long bookId, Principal principal) {
		if (principal.getName() == null) {
			return "You Must Login to delete book in shopping cart.";
		}

		String name = principal.getName();
		User user = userRepo.findByEmail(name).get();

		cartService.removeBook(bookId, user);

		return "The product has been removed from shopping cart";

	}
}

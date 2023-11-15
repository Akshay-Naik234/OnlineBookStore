package com.springboot.bookStore.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.springboot.bookStore.entity.Address;
import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.entity.cart_items;
import com.springboot.bookStore.repository.AddressRepository;
import com.springboot.bookStore.repository.UserRepository;
import com.springboot.bookStore.service.ShoppingCartService;

@Controller
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService cartService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AddressRepository addressRepo;

	@GetMapping("/user/cart")
	public String showShoppingCart(Model model, Principal principal) {

		String name = principal.getName();
		User user = userRepo.findByEmail(name).get();

		List<cart_items> cartItems = cartService.listCartItems(user);
		Address address = addressRepo.findByUser(user);
		if (address == null) {
			model.addAttribute("address", "null");
		} else {
			model.addAttribute("address", "notnull");
		}
		float total = 0f;
		for (cart_items cart_item : cartItems) {
			total = total + (float) ((cart_item.getBook().getPrice()) * (cart_item.getQuantity()));
		}
		model.addAttribute("total", total);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("title", "Shopping Cart");

		return "user/shopping_cart";
	}

}

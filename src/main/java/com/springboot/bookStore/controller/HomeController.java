package com.springboot.bookStore.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.helper.Message;
import com.springboot.bookStore.repository.OrderDetailsRepository;
import com.springboot.bookStore.repository.UserRepository;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@Autowired
	private AdminController adminController;

	@RequestMapping("/about")
	public String aboutUsPage() {
		return "about";
	}

	@RequestMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("title", "signup - Online Book Store");
		model.addAttribute("user", new User());
		return "signup";
	}

	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session, @RequestParam("userImage") MultipartFile file) {
		try {
			if (!agreement) {
				throw new Exception("You have not agreed the terms and conditions");
			}
			String email = user.getEmail();
			Optional<User> findByEmail = userRepository.findByEmail(email);
			if (findByEmail.isPresent()) {
				throw new Exception("Email Address Already exists");
			}

			if (result.hasErrors()) {
				System.out.println("ERROR " + result.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			try {
				user.setImage(Base64.getEncoder().encodeToString(file.getBytes()));

			} catch (IOException e) {
				e.printStackTrace();
			}

			user.setRole("ROLE_USER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			User result1 = userRepository.save(user);

//			System.out.println("User " + result1);

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered", "success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "danger"));
			return "signup";
		}
		return "signup";
	}

	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title", "Login Page");
		return "login";
	}

}

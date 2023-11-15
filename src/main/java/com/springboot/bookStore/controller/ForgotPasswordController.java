package com.springboot.bookStore.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.repository.UserRepository;
import com.springboot.bookStore.service.Impl.EmailService;

@Controller
public class ForgotPasswordController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepo;

	@RequestMapping("/forgot")
	public String openEmailForm() {
		return "forgot_email_form";
	}

	public char[] generatorOTP(int length) {
		// Creating object of Random class
		Random obj = new Random();
		char[] otp = new char[length];
		for (int i = 0; i < length; i++) {
			otp[i] = (char) (obj.nextInt(10) + 48);
		}
		return otp;
	}

	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		char[] otp = generatorOTP(4);
		String str = String.valueOf(otp);
		int i = Integer.parseInt(str);
		System.out.println("String " + i);
		String subject = "OTP From Online Book Store";
		String message = "OTP = " + i;
		String to = email;
		boolean flag = emailService.sendMail(subject, message, to);

		if (flag) {
			session.setAttribute("myotp", i);
			session.setAttribute("email", to);
			return "verify_otp";
		} else {
			session.setAttribute("message", "Enter correct email address");
			return "forgot_email_form";
		}
	}

	// verify otp

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {
		int myotp = (int) session.getAttribute("myotp");
		String email = (String) session.getAttribute("email");
		if (myotp == otp) {
			User user = userRepo.getUserByUserName(email);
			if (user == null) {
				session.setAttribute("message", "User does not exists with this email");
				return "forgot_email_form";
			} else {

			}
			return "password_change_form";
		} else {
			session.setAttribute("message", "You have entered wrong otp !!");
			return "verify_otp";

		}
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) {
		String email = (String) session.getAttribute("email");
		User user = userRepo.getUserByUserName(email);
		user.setPassword(passwordEncoder.encode(newpassword));
		userRepo.save(user);

		return "redirect:/signin?change=password changed successfully";
	}

}

package com.springboot.bookStore.config;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.springboot.bookStore.entity.User;
import com.springboot.bookStore.repository.UserRepository;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		String redirectURL = request.getContextPath();
		String role = userDetails.getRole();
		if (role.equals("ROLE_ADMIN")) {
			redirectURL += "/admin/books/0";
		} else {
			redirectURL += "/user/books/0";
		}

		response.sendRedirect(redirectURL);
	}

}

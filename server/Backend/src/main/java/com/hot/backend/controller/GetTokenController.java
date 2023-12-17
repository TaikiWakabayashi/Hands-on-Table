package com.hot.backend.controller;

import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class GetTokenController {
	
	@GetMapping(value="/csrftoken")
	public String getCsrfToken(HttpServletRequest request) {
		
		DefaultCsrfToken token = (DefaultCsrfToken)request.getAttribute("_csrf");
		
		if(token == null) {
			throw new RuntimeException("could not get a token");
		}
		return token.getToken();
	}
}

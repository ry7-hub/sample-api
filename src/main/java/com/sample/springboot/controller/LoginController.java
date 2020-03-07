package com.sample.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.springboot.entity.User;
import com.sample.springboot.exception.BadRequestException;
import com.sample.springboot.response.UserResponse;
import com.sample.springboot.service.UserService;

import io.jsonwebtoken.Jwts;

import static com.sample.springboot.support.SecurityConstants.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

	@Autowired
	UserService userService;

	@GetMapping("/user")
	public UserResponse login(@RequestHeader(HEADER_STRING) String token) throws Exception {

		String email = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody().getSubject();

		User user = userService.findByEmail(email);

		if (user == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), "トークンが不正");
		}

		UserResponse res = new UserResponse();
		res.setId(user.getId());
		res.setName(user.getName());
		res.setEmail(user.getEmail());

		return res;
	}

}

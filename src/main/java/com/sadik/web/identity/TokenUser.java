package com.sadik.web.identity;

import org.springframework.security.core.authority.AuthorityUtils;

import com.sadik.web.model.user.User;

public class TokenUser extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;
	private User user;

	// For returning a normal user
	public TokenUser(User user) {
		super(user.getUserId(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getRole() {
		return user.getRole().toString();
	}
}

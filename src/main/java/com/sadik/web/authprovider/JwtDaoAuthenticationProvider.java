package com.sadik.web.authprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Component;

import com.sadik.web.service.user.UserDetailsService;

@Component
public class JwtDaoAuthenticationProvider extends DaoAuthenticationProvider{
	
	@Autowired
	public JwtDaoAuthenticationProvider(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}
}

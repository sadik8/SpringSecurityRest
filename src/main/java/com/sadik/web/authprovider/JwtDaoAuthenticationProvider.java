package com.sadik.web.authprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Component;

import com.sadik.web.service.user.UserDetailsService;

@Component
@Primary
public class JwtDaoAuthenticationProvider extends DaoAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	public JwtDaoAuthenticationProvider(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}
}

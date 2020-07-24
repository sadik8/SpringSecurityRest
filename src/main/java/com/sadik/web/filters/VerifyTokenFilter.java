package com.sadik.web.filters;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.sadik.web.service.token.TokenService;

import io.jsonwebtoken.JwtException;

/*
This filter checks if there is a token in the Request service header and the token is not expired
it is applied to all the routes which are protected
*/
public class VerifyTokenFilter extends GenericFilterBean {

	private final TokenService tokenService;
	// private AuthenticationFailureHandler loginFailureHandler = new
	// SimpleUrlAuthenticationFailureHandler();

	public VerifyTokenFilter(TokenService tokenUtil) {
		this.tokenService = tokenUtil;
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			Optional<Authentication> authentication = tokenService.verifyToken(request);
			if (authentication.isPresent()) {
				SecurityContextHolder.getContext().setAuthentication(authentication.get());
			} else {
				SecurityContextHolder.getContext().setAuthentication(null);
			}
			filterChain.doFilter(req, res);
		} catch (JwtException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} finally {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
	}

}

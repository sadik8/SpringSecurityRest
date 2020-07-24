package com.sadik.web.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sadik.web.identity.TokenUser;
import com.sadik.web.model.response.OperationResponse.ResponseStatusEnum;
import com.sadik.web.model.session.SessionItem;
import com.sadik.web.model.session.SessionResponse;
import com.sadik.web.service.token.TokenService;

/* This filter maps to /session and tries to validate the username and password */
public class JwtAuthenticationProviderFilter extends AbstractAuthenticationProcessingFilter {

	private TokenService tokenService;

	public JwtAuthenticationProviderFilter(String urlMapping, AuthenticationManager authenticationManager,
			TokenService tokenService) {
		super(new AntPathRequestMatcher(urlMapping));
		setAuthenticationManager(authenticationManager);
		this.tokenService = tokenService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException, JSONException {
		try {
			String jsonString = IOUtils.toString(request.getInputStream(), "UTF-8");
			/* using org.json */
			JSONObject userJSON = new JSONObject(jsonString);
			String username = userJSON.getString("username");
			String password = userJSON.getString("password");
			String browser = request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "";
			String ip = request.getRemoteAddr();

			// final UsernamePasswordAuthenticationToken loginToken = new
			// UsernamePasswordAuthenticationToken("demo", "demo");
			final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,
					password);
			return getAuthenticationManager().authenticate(authToken); // This will take to successfulAuthentication or
																		// faliureAuthentication function
		} catch (JSONException | AuthenticationException e) {
			throw new AuthenticationServiceException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication authToken) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authToken);

		TokenUser tokenUser = (TokenUser) authToken.getPrincipal();
		SessionResponse resp = new SessionResponse();
		SessionItem respItem = new SessionItem();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String tokenString = tokenService.createTokenForUser(tokenUser);

		respItem.setFirstName(tokenUser.getUser().getFirstName());
		respItem.setLastName(tokenUser.getUser().getLastName());
		respItem.setUserId(tokenUser.getUser().getUserId());
		respItem.setEmail(tokenUser.getUser().getEmail());
		respItem.setToken(tokenString);

		resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
		resp.setOperationMessage("Login Success");
		resp.setItem(respItem);
		String jsonRespString = ow.writeValueAsString(resp);

		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType("applicatoin/json");
		res.getWriter().write(jsonRespString);
		// res.getWriter().write(jsonResp.toString());
		res.getWriter().flush();
		res.getWriter().close();

		// DONT call supper as it contine the filter chain
		// super.successfulAuthentication(req, res, chain, authResult);
	}
}

package com.sadik.web.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sadik.web.authprovider.JwtDaoAuthenticationProvider;
import com.sadik.web.filters.CorsFilter;
import com.sadik.web.filters.JwtAuthenticationProviderFilter;
import com.sadik.web.filters.VerifyTokenFilter;
import com.sadik.web.service.token.TokenService;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private JwtDaoAuthenticationProvider authProvider;

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays.asList(authProvider));
	}

	// .csrf() is optional, enabled by default, if using
	// WebSecurityConfigurerAdapter constructor
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling().and().anonymous().and()
				// Disable Cross site references
				.csrf().disable()
				// Add CORS Filter
				.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
				// Custom Token based authentication based on the header previously given to the
				// client
				.addFilterBefore(new VerifyTokenFilter(tokenService), UsernamePasswordAuthenticationFilter.class)
				// custom JSON based authentication by POST of
				// {"username":"<name>","password":"<password>"} which sets the token header
				// upon authentication
				.addFilterBefore(new JwtAuthenticationProviderFilter("/session", authenticationManager(), tokenService),
						UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests().anyRequest().authenticated();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Filters will not get executed for the resources
		web.ignoring().antMatchers("/", "/resources/**", "/static/**", "/public/**", "/webui/**", "/h2-console/**",
				"/configuration/**", "/swagger-ui/**", "/swagger-resources/**", "/api-docs", "/api-docs/**",
				"/v2/api-docs/**", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
				"/**/*.gif", "/**/*.svg", "/**/*.ico", "/**/*.ttf", "/**/*.woff", "/**/*.otf");
	}
}

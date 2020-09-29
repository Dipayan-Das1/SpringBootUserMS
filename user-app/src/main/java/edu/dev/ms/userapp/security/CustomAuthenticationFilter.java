package edu.dev.ms.userapp.security;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dev.ms.userapp.dto.UserLoginDto;
import edu.dev.ms.userapp.dto.UserResponseDto;
import edu.dev.ms.userapp.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


/*
 * Processes an authentication form submission
Login forms must present two parameters to this filter: a username and password. 
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;

	private ObjectMapper objectMapper;
	
	private UserService userService;
	
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager,ObjectMapper objectMapper,UserService userService)
	{
		this.authenticationManager = authenticationManager;
		this.objectMapper = objectMapper;
		this.userService = userService;
	}

	///Called 1st
	///this method intercepts and forms the token to authenticate from the params passed by user 
	// loadByUsername populates from DB and matches password and on success passes the loaded UserDetails (UserPrincipal) class to  successfulAuthentication
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("Inside attemptAuthentication:UsernamePasswordAuthenticationFilter ");
		try {
			UserLoginDto loginDto =objectMapper.readValue(request.getInputStream(), UserLoginDto.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword(),new ArrayList<>()));
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
	///this method sets JWT token in response header on successful authentication
	///Called 3rd
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("Inside successfulAuthentication:UsernamePasswordAuthenticationFilter");
		String username = ((UserPrincipal)authResult.getPrincipal()).getUsername();
		String token = Jwts.builder()
							.setSubject(username)
							.setExpiration(new Date(Instant.now().plusMillis(SecurityConstants.EXPIRATION_TIME).toEpochMilli()))
							.signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET) /// this creates a JWS token
							.compact();
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+token);
		UserResponseDto respData = userService.getUserByEmail(username);
		response.setHeader("userID", respData.getUserId());
	}
}

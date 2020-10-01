package edu.dev.ms.userapp.service.impl;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Component;

import edu.dev.ms.userapp.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenServiceImpl {

	public String generateEmailToken(String email)
	{
		String token = Jwts.builder()
				.setSubject(email)
				.setExpiration(new Date(Instant.now().plusMillis(SecurityConstants.EMAIL_EXPIRATION_TIME).toEpochMilli()))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET) /// this creates a JWS token
				.compact();
		return token;
	}
	
	public String generatePasswordResetToken(String email)
	{
		String token = Jwts.builder()
				.setSubject(email)
				.setExpiration(new Date(Instant.now().plusMillis(SecurityConstants.PWD_RESET_EXPIRATION_TIME).toEpochMilli()))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET) /// this creates a JWS token
				.compact();
		return token;
	}
	
	public boolean isEmailTokenVaid(String token)
	{
		Claims claim = Jwts.parser()
				.setSigningKey(SecurityConstants.TOKEN_SECRET)
				.parseClaimsJws(token).getBody();
		Date expirationTime = claim.getExpiration();
		return 	Instant.now().isBefore(Instant.ofEpochMilli(expirationTime.getTime()));	
	}
	
	public boolean isResetPasswordTokenVaid(String token)
	{
		Claims claim = Jwts.parser()
				.setSigningKey(SecurityConstants.TOKEN_SECRET)
				.parseClaimsJws(token).getBody();
		Date expirationTime = claim.getExpiration();
		boolean valid =	Instant.now().isBefore(Instant.ofEpochMilli(expirationTime.getTime()));
		return valid;
	}
}

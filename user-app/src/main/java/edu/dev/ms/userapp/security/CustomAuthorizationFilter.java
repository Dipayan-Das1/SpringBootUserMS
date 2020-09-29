package edu.dev.ms.userapp.security;

import java.io.IOException;
import java.util.LinkedList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import edu.dev.ms.userapp.entity.UserEntity;
import edu.dev.ms.userapp.repository.UserRepository;
import io.jsonwebtoken.Jwts;


/*
 * Processes a HTTP request's BASIC authorization headers, putting the result into the SecurityContextHolder.
Any realm name presented in the HTTP request is ignored.
In summary, this filter is responsible for processing any request that has a HTTP request header of Authorization with an authentication scheme of Basic and a Base64-encoded username:password token
 */
public class CustomAuthorizationFilter extends BasicAuthenticationFilter{

	private UserRepository userRepository;
	public CustomAuthorizationFilter(AuthenticationManager authenticationManager,UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Inside doFilterInternal:CustomAuthorizationFilter");
		String auth = request.getHeader(SecurityConstants.HEADER_STRING);
		System.out.println(auth);
		
		if(auth== null || !auth.startsWith(SecurityConstants.TOKEN_PREFIX))
		{
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken token = getParsedTokenFromBearerToken(request);
		SecurityContextHolder.getContext().setAuthentication(token);
		
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getParsedTokenFromBearerToken(HttpServletRequest request)
	{
		String bearerToken = request.getHeader(SecurityConstants.HEADER_STRING);
		String token = bearerToken.replace(SecurityConstants.TOKEN_PREFIX, "");
		System.out.println(token);
		String userEmail = Jwts.parser()
						.setSigningKey(SecurityConstants.TOKEN_SECRET)
						.parseClaimsJws(token)
						.getBody()
						.getSubject();
		System.out.println(userEmail);
		if(userEmail!=null)
		{
			UserEntity user = userRepository.findByEmail(userEmail); 
			if(user == null) return null;
			
			return new UsernamePasswordAuthenticationToken(userEmail, null,new UserPrincipal(user).getAuthorities());
		}
		return null;
	}

}

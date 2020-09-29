package edu.dev.ms.userapp.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.dev.ms.userapp.repository.UserRepository;
import edu.dev.ms.userapp.service.UserService;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	
	private UserService userDetailsService;
	
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	private ObjectMapper objectMapper;
	
	private UserRepository userRepository;
	
	public WebSecurity(UserService userDetailsService,BCryptPasswordEncoder bcryptPasswordEncoder,ObjectMapper mapper,UserRepository userRepository)
	{
		this.userDetailsService = userDetailsService;
		this.bcryptPasswordEncoder = bcryptPasswordEncoder;
		this.objectMapper = mapper;
		this.userRepository = userRepository;
		
	}
	
	///manage http security allowing access to sign up url
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		System.out.println("Inside configure:WebSecurity http");
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL)
		.permitAll()
		.antMatchers(HttpMethod.GET,SecurityConstants.VERIFICATION_URL)
		.permitAll()
		.anyRequest().authenticated().and()
		.addFilter(new CustomAuthenticationFilter(authenticationManager(), objectMapper,userDetailsService))
		.addFilter(new CustomAuthorizationFilter(authenticationManager(),userRepository))
		//to prevent sessions from caching credentials or tokens
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		//new AuthenticationFilter custom created, authenticationManager is a method in the WebSecurityConfigurerAdapter class
	}
	
	
	//set the password encoder
	//userDetailsService needs to provide / override the load by loadUserByUsername method
	//configure AuthenticationManagerBuilder
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("Inside configure:AuthenticationManagerBuilder ");
		auth.userDetailsService(userDetailsService).passwordEncoder(bcryptPasswordEncoder);
	}

}

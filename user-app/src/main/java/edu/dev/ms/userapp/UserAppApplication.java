package edu.dev.ms.userapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)      //for allowing method level security annotations prePostEnabled = true (enable @preauthorize),securedEnabled = true enab;e @Secured
@SpringBootApplication
public class UserAppApplication extends SpringBootServletInitializer{

	/*
	 * @Override protected SpringApplicationBuilder
	 * configure(SpringApplicationBuilder builder) { return
	 * builder.sources(UserAppApplication.class); }
	 */
	public static void main(String[] args) {
		SpringApplication.run(UserAppApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}

}

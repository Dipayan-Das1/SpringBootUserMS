package edu.dev.ms.userapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	/*
	 * @Override public void addCorsMappings(CorsRegistry registry) { // TODO
	 * Auto-generated method stub
	 * registry.addMapping("/user/verify").allowedOrigins("http://localhost:9000");
	 * // registry.addMapping("/**").allowedOrigins("http://localhost:9000"); // to
	 * all endpoints //.allowedOrigins("*") means from all origins }
	 */
}

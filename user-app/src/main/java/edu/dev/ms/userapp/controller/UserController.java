package edu.dev.ms.userapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.dev.ms.userapp.dto.MessageDto;
import edu.dev.ms.userapp.dto.UserDto;
import edu.dev.ms.userapp.dto.UserResponseDto;
import edu.dev.ms.userapp.exception.UserExistsException;
import edu.dev.ms.userapp.exception.UserNotFoundException;
import edu.dev.ms.userapp.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	@Autowired
	private UserService userService;

	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
	@GetMapping(path = "/{userId}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public UserResponseDto getUser(@PathVariable String userId) throws UserNotFoundException
	{
		log.info("Inside get user {}",userId);
		UserResponseDto  userDto = userService.getUser(userId);
		return  userDto;
	}
	
	@PostMapping(path="/signup",consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public MessageDto createUser(@Valid @RequestBody UserDto user) throws UserExistsException
	{
		log.info("Inside create user {}",user.toString());
		UserResponseDto createduser = userService.createUser(user);
		return new MessageDto(String.format("User Created successfully with id %s", createduser.getUserId())) ;
	}
	
	@PutMapping(path = "/{userId}",consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserResponseDto updateUser(@PathVariable String userId,@Valid @RequestBody UserDto user)
	{
		log.info("Inside update user {}",userId);
		UserResponseDto  userDto = userService.updateUser(userId, user);
		return userDto;
	}
	
	//@Secured("ROLE_ADMIN")
	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
	@DeleteMapping(path = "/{userId}")
	public MessageDto deleteUser(@PathVariable String userId)
	{
		log.info("Inside delete user {}",userId);
		userService.deleteUser(userId);
		return new MessageDto("User Deleted successfully");
	}
	
	@GetMapping(path="/verify")
	//@CrossOrigin(origins = "*")///allows access to all
	//@CrossOrigin(origins="http://localhost:9000")
	public MessageDto verifyUser(@RequestParam(name = "token") String token) throws UserExistsException
	{
		log.info("Inside verify user {}");
		boolean verified = userService.verifyEmailAddress(token);
		if(verified)
		{
			return new MessageDto("Email verified");
		}
		else
		{
			throw new IllegalArgumentException("Email token verification failed");
		}
	}
	
	
}

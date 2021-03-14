package edu.dev.ms.userapp.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.dev.ms.userapp.dto.UserResponseDto;
import edu.dev.ms.userapp.service.UserService;

@RequestMapping("/users")
@RestController
public class UserListController {
	
	@Autowired
	private UserService userService;

	@GetMapping
	
	public List<UserResponseDto> getUsers(@RequestParam(name = "page",defaultValue = "0")  Integer page,
			@RequestParam(name="limit",defaultValue = "10")  Integer limit)
	{
		if(page < 0)
		{
			throw new IllegalArgumentException("Page cannot be less than 0");
		}
		
		if(limit < 0)
		{
			throw new IllegalArgumentException("Limit cannot be less than 0");
		}
		
		return userService.getUsers(page, limit);
	}
}

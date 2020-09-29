package edu.dev.ms.userapp.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dev.ms.userapp.dto.AddressDto;
import edu.dev.ms.userapp.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users/{userId}/addresses")
@Slf4j
public class AddressController {
	
	@Autowired
	private UserService userService;

	@PreAuthorize("hasAuthority('READ_AUTHORITY')")
	@GetMapping("/{userId}/addresses")
	public CollectionModel<AddressDto> getUserAddresses(@PathVariable String userId)
	{
		log.info("Inside get user addresses for user {}",userId);
		List<AddressDto> addresses = userService.getUserAddress(userId);
		
		if(addresses!=null)
		{
			addresses.forEach(address -> {
				Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(getClass()).getUserAddress(userId, address.getAddressId())).withSelfRel();
				address.add(selfLink);
			});	
		}
		
		Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(userId)).withRel("user");
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(getClass()).getUserAddresses(userId)).withSelfRel();
		return CollectionModel.of(addresses,Arrays.asList(userLink,selfLink));
	}
	
	@PreAuthorize("hasAuthority('READ_AUTHORITY')")
	@GetMapping("/{userId}/addresses/{addressId}")
	public AddressDto getUserAddress(@PathVariable String userId,@PathVariable String addressId)
	{
		log.info("Inside get user addresses for user {}",userId);
		AddressDto addressDto = userService.getUserAddressById(userId, addressId);
		Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(userId)).withRel("user");
		addressDto.add(userLink);
		Link userAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(getClass()).getUserAddresses(userId)).withRel("addresses");
		addressDto.add(userAddressesLink);
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(getClass()).getUserAddress(userId, addressId)).withSelfRel();
		addressDto.add(selfLink);
		return addressDto;
		
	}

}

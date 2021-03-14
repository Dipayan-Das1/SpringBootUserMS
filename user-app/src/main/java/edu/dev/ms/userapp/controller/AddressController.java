package edu.dev.ms.userapp.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dev.ms.userapp.dto.AddressDto;
import edu.dev.ms.userapp.dto.MessageDto;
import edu.dev.ms.userapp.service.AddressService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users/{userId}/addresses")
@Slf4j
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	


	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
	@GetMapping()
	public CollectionModel<AddressDto> getUserAddresses(@PathVariable String userId)
	{
		log.info("Inside get user addresses for user {}",userId);
		List<AddressDto> addresses = addressService.getUserAddress(userId);
		
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
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
	@GetMapping("/{addressId}")
	public AddressDto getUserAddress(@PathVariable String userId,@PathVariable String addressId)
	{
		log.info("Inside get user addresses for user {}",userId);
		AddressDto addressDto = addressService.getUserAddressById(userId, addressId);
		Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(userId)).withRel("user");
		addressDto.add(userLink);
		Link userAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(getClass()).getUserAddresses(userId)).withRel("addresses");
		addressDto.add(userAddressesLink);
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(getClass()).getUserAddress(userId, addressId)).withSelfRel();
		addressDto.add(selfLink);
		return addressDto;	
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
	@PostMapping
	public MessageDto addAddress(@PathVariable String userId,@Valid @RequestBody AddressDto address)
	{
		log.info("Add address for user {}",userId);
		address = addressService.addAddress(userId, address);
		return new MessageDto(String.format("New address created with id %s",address.getAddressId()));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
	@PutMapping("/{addressId}")
	public AddressDto updateAddress(@PathVariable(required = true) String userId,@PathVariable(required = true) String addressId,@Valid @RequestBody AddressDto address)
	{
		log.info("Update address {} for user {}",addressId,userId);
		address.setAddressId(addressId);
		address = addressService.updateAddress(userId, address);
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AddressController.class).getUserAddress(userId,addressId)).withSelfRel();
		address.add(selfLink);
		return address;

	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.userId")
	@DeleteMapping("/{addressId}")
	public MessageDto deleteAddress(@PathVariable String userId,@PathVariable String addressId)
	{
		log.info("Delete address {} for user {}",addressId,userId);
		addressService.deleteAddress(userId, addressId);
		return new MessageDto("Address deleted successfully");
	}
	
	

}

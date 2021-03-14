package edu.dev.ms.userapp.service;

import java.util.List;

import edu.dev.ms.userapp.dto.AddressDto;

public interface AddressService {

	List<AddressDto> getUserAddress(String userId);

	AddressDto getUserAddressById(String userId, String addressId);

	AddressDto addAddress(String userId, AddressDto addressDto);
	
	AddressDto updateAddress(String userId, AddressDto addressDto);
	
	void deleteAddress(String userId, String addressId);
	
	
}

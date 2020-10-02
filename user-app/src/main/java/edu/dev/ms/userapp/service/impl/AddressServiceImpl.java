package edu.dev.ms.userapp.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dev.ms.userapp.dto.AddressDto;
import edu.dev.ms.userapp.entity.AddressEntity;
import edu.dev.ms.userapp.entity.UserEntity;
import edu.dev.ms.userapp.exception.AddressNotFoundException;
import edu.dev.ms.userapp.exception.UserNotFoundException;
import edu.dev.ms.userapp.repository.AddressRepository;
import edu.dev.ms.userapp.repository.UserRepository;
import edu.dev.ms.userapp.service.AddressService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	static ModelMapper modelMapperAddressDto = new ModelMapper();
	static {
		
		modelMapperAddressDto.addMappings(new PropertyMap<AddressDto, AddressEntity>() {
		                @Override
		                protected void configure() {
		                    skip(destination.getId());
		                }
		            });
		
	}


	@Override
	public List<AddressDto> getUserAddress(String userId)
	{
		UserEntity user = userRepository.findByUserId(userId);
		if(user==null)
		{
			throw  UserNotFoundException.userNotFoundExceptionByUserId(userId);
		}
		List<AddressEntity> addresses = user.getAddresses();
		
		List<AddressDto> addressesList = new LinkedList<>();
		if(addresses!=null)
		{
			addressesList = addresses.stream().map(addrEnity -> {
				return modelMapper.map(addrEnity, AddressDto.class);
			}).collect(Collectors.toList());
		}
		return addressesList;
	}
	
	@Override
	public AddressDto getUserAddressById(String userId,String addressId)
	{
		UserEntity user = userRepository.findByUserId(userId);
		if(user==null)
		{
			throw  UserNotFoundException.userNotFoundExceptionByUserId(userId);
		}
		AddressEntity address = addressRepository.findByAddressId(addressId);
		if(address == null)
		{
			throw  AddressNotFoundException.addressNotFoundByID(addressId);
		}
		if(!address.getUser().getId().equals(user.getId()))
		{
			throw  AddressNotFoundException.addressNotFoundByID(addressId);
		}	
		
		return modelMapper.map(address, AddressDto.class);
			
	}

	@Override
	@Transactional(readOnly = false)
	public AddressDto addAddress(String userId, AddressDto addressDto) {
		UserEntity user = userRepository.findByUserId(userId);
		if(user==null)
		{
			throw  UserNotFoundException.userNotFoundExceptionByUserId(userId);
		}
		
		AddressEntity address = modelMapper.map(addressDto, AddressEntity.class);
		address.setAddressId(UUID.randomUUID().toString());
		address.setUser(user);
		
		addressRepository.save(address);
		log.info("Address added successfully with id {}",address.getAddressId());
		return modelMapper.map(address, AddressDto.class);
		
	}

	@Override
	@Transactional(readOnly = false)
	public AddressDto updateAddress(String userId, AddressDto addressDto) {
		AddressEntity address = addressRepository.findByAddressId(addressDto.getAddressId());
		if(address == null)
		{
			throw new IllegalArgumentException(String.format("Address with id %s not found",addressDto.getAddressId())); 
		}
		if(!address.getUser().getUserId().equals(userId))
		{
			throw new IllegalArgumentException(String.format("Address with id %s does not belong to user with id %s ",addressDto.getAddressId(),userId)) ;
		}
		modelMapperAddressDto.map(addressDto,address);
		addressRepository.save(address);
		log.info("Address updated successfully with id {}",address.getAddressId());
		return addressDto;
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAddress(String userId, String addressId) {
		AddressEntity address = validateAndReturnAddress(userId,addressId);
		addressRepository.delete(address);
		log.info("Address updated successfully with id {}",addressId);
	}
	
	private AddressEntity validateAndReturnAddress(String userId, String addressId)
	{
		AddressEntity address = addressRepository.findByAddressId(addressId);
		if(address == null)
		{
			throw new IllegalArgumentException(String.format("Address with id %s not found",addressId)); 
		}
		if(!address.getUser().getUserId().equals(userId))
		{
			throw new IllegalArgumentException(String.format("Address with id %s does not belong to user with id %s ",addressId,userId)) ;
		}
		return address;
	}
}

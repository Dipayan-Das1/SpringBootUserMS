package edu.dev.ms.userapp.service.impl;



import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
//import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dev.ms.userapp.dto.AddressDto;
import edu.dev.ms.userapp.dto.UserDto;
import edu.dev.ms.userapp.dto.UserResponseDto;
import edu.dev.ms.userapp.entity.AddressEntity;
import edu.dev.ms.userapp.entity.UserEntity;
import edu.dev.ms.userapp.exception.AddressNotFoundException;
import edu.dev.ms.userapp.exception.UserExistsException;
import edu.dev.ms.userapp.exception.UserNotFoundException;
import edu.dev.ms.userapp.repository.AddressRepository;
import edu.dev.ms.userapp.repository.UserRepository;
import edu.dev.ms.userapp.security.UserPrincipal;
import edu.dev.ms.userapp.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;
	

	@Override
	@Transactional(readOnly = false)
	public UserResponseDto createUser(UserDto user) throws UserExistsException {
		UserEntity userExist = userRepository.findByEmail(user.getEmail());
		if(userExist!=null)
		{
			throw new UserExistsException(String.format("User with email %s already exists", user.getEmail()));
		}
		UUID uuid = UUID.randomUUID();
		UserEntity userEntity = new UserEntity();
		modelMapper.map(user, userEntity);
		//BeanUtils.copyProperties(user, userEntity);
		userEntity.setUserId(uuid.toString());
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
		if(userEntity.getAddresses()!=null)
		{
			for(AddressEntity addr : userEntity.getAddresses())
			{
				addr.setAddressId(UUID.randomUUID().toString());
				addr.setUser(userEntity);
			}
		}
		userRepository.save(userEntity);
		log.info("User created successfully {}",userEntity.toString());
		UserResponseDto response = new UserResponseDto();
		//BeanUtils.copyProperties(userEntity, response);
		modelMapper.map(userEntity, response);
		return response;
	}

	@Override
	public UserResponseDto getUser(String userId) throws UserNotFoundException {
		
		UserEntity user = userRepository.findByUserId(userId);
		if(user == null)
		{
			throw UserNotFoundException.userNotFoundExceptionByUserId(userId);
			
		}
		UserResponseDto response = new UserResponseDto();
		
		modelMapper.map(user, response);
		log.info("User found {}",user.toString());
		return response;
	}
	
	@Override
	public UserResponseDto getUserByEmail(String email) throws UserNotFoundException {
		
		UserEntity user = userRepository.findByEmail(email);
		if(user == null)
		{
			throw UserNotFoundException.userNotFoundExceptionByEmail(email);
			
		}
		UserResponseDto response = new UserResponseDto();
		BeanUtils.copyProperties(user,response);
		log.info("User found {}",user.getUserId());
		return response;
	}

	@Override
	public UserResponseDto updateUser(String userId, UserDto user) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity==null)
		{
			throw  UserNotFoundException.userNotFoundExceptionByUserId(userId);
		}
		
		BeanUtils.copyProperties(user, userEntity,"email");
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
		userRepository.save(userEntity);
		log.info("User updated successfully {}",userEntity.toString());
		UserResponseDto response = new UserResponseDto();
		BeanUtils.copyProperties(userEntity, response);
		return response;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity==null)
		{
			throw  UserNotFoundException.userNotFoundExceptionByUserId(userId);
		}
		userRepository.delete(userEntity);
		log.info("User deleted successfully");
	}

	////For spring security
	///username is email in our case
	@Override
	public UserDetails loadUserByUsername(String email)   {
		System.out.println("Inside loadUserByUsername:UserServiceImpl");
		UserEntity userExist = userRepository.findByEmail(email);
		if(userExist!=null)
		{
			return new UserPrincipal(userExist);
		}
		else
		{
			
				throw new UsernameNotFoundException(email);
		
		}
	}
	
	@Override
	public List<UserResponseDto> getUsers(int pageNo,int limit)
	{
		PageRequest page = PageRequest.of(pageNo, limit);
		Page<UserEntity> users = userRepository.findAll(page);
		List<UserResponseDto> userList = users.stream().map(user -> {
			UserResponseDto response = new UserResponseDto();
			BeanUtils.copyProperties(user, response);
			return response;
		}).collect(Collectors.toList());
		return userList;
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
	
}

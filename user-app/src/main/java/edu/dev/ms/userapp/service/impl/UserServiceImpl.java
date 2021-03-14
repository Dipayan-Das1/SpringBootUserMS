package edu.dev.ms.userapp.service.impl;



import java.util.Arrays;
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
import edu.dev.ms.userapp.entity.RoleEntity;
import edu.dev.ms.userapp.entity.UserEntity;
import edu.dev.ms.userapp.exception.AddressNotFoundException;
import edu.dev.ms.userapp.exception.UserExistsException;
import edu.dev.ms.userapp.exception.UserNotFoundException;
import edu.dev.ms.userapp.repository.AddressRepository;
import edu.dev.ms.userapp.repository.RoleRepository;
import edu.dev.ms.userapp.repository.UserRepository;
import edu.dev.ms.userapp.security.UserPrincipal;
import edu.dev.ms.userapp.service.EmailService;
import edu.dev.ms.userapp.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private TokenServiceImpl tokenService;
	
	@Autowired
	private EmailService emailService;
	

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
		userEntity.setEmailVerificationToken(tokenService.generateEmailToken(user.getEmail()));
		RoleEntity role = roleRepository.findByName("ROLE_USER");
		userEntity.setRoles(Arrays.asList(role));
		userRepository.save(userEntity);
		emailService.verifyEmail(userEntity.getEmail(),userEntity.getEmailVerificationToken());
		log.info("User created successfully {}",userEntity.getUserId());
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

	public boolean verifyEmailAddress(String token)
	{
		boolean verified = false;
		UserEntity user = userRepository.findByEmailVerificationToken(token);
		if(user!=null)
		{
			boolean isTokenValid = tokenService.isEmailTokenVaid(token);
			if(isTokenValid)
			{
				user.setEmailVerificationStatus(true);
				user.setEmailVerificationToken(null);
				userRepository.save(user);
				verified = true;
				log.debug("User verified {}",user.getEmail());
			}
			else
			{
				log.debug("User cannot be verified {}",user.getEmail());
			}
		}
		return verified;
	}
	
}

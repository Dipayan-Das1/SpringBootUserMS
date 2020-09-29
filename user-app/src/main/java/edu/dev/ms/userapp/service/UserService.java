package edu.dev.ms.userapp.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import edu.dev.ms.userapp.dto.AddressDto;
import edu.dev.ms.userapp.dto.UserDto;
import edu.dev.ms.userapp.dto.UserResponseDto;
import edu.dev.ms.userapp.exception.UserExistsException;
import edu.dev.ms.userapp.exception.UserNotFoundException;

public interface UserService extends UserDetailsService{
public UserResponseDto createUser(UserDto user) throws UserExistsException;
public UserResponseDto getUser(String userId) throws UserNotFoundException;
public UserResponseDto updateUser(String userId,UserDto user);
public void deleteUser(String userId);
UserResponseDto getUserByEmail(String email) throws UserNotFoundException;
List<UserResponseDto> getUsers(int pageNo, int limit);
List<AddressDto> getUserAddress(String userId);
AddressDto getUserAddressById(String userId, String addressId);
boolean verifyEmailAddress(String token);
}

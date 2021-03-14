package edu.dev.ms.userapp.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	@NotEmpty(message = "Firstname cannot be empty")
	private String firstName;
	private String lastName;
	@Email
	private String email;
	@NotEmpty(message="Password cannot be empty")
	private String password;
	private List<AddressDto> addresses;

}

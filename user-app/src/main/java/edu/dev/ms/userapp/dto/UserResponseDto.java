package edu.dev.ms.userapp.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
	private String firstName;
	private String lastName;
	private String email;
	private String userId;
	private List<AddressDto> addresses;
}

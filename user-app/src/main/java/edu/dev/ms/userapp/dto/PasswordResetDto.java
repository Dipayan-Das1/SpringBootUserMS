package edu.dev.ms.userapp.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDto {
	@NotBlank(message = "Token must be present")
	private String token;
	@NotBlank(message="Password cannot be empty")
	private String password;
}

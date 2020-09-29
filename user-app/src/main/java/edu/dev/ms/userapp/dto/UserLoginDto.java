package edu.dev.ms.userapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLoginDto {
	
	private String userName;
	private String password;

}

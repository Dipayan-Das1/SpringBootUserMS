package edu.dev.ms.userapp.exception;

public class UserNotFoundException extends RuntimeException{
	
	public UserNotFoundException(String message)
	{
		super(message);
	}
	
	public static UserNotFoundException userNotFoundExceptionByEmail(String email)
	{
		return new UserNotFoundException(String.format("User not found with email %s ",email));
	}
	
	public static UserNotFoundException userNotFoundExceptionByUserId(String userId)
	{
		return new UserNotFoundException(String.format("User not found with Id %s ",userId));
	}

}

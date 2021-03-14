package edu.dev.ms.userapp.exception;

public class AddressNotFoundException extends RuntimeException{
	
	public AddressNotFoundException(String message)
	{
		super(message);
	}
	
	public static AddressNotFoundException addressNotFoundByID(String addressId)
	{
		return new AddressNotFoundException(String.format("Address not found by id {}", addressId)) ;
	}

}

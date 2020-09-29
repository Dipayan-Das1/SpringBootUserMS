package edu.dev.ms.userapp.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.dev.ms.userapp.dto.MessageDto;
import edu.dev.ms.userapp.exception.UserExistsException;
import edu.dev.ms.userapp.exception.UserNotFoundException;

@ControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<MessageDto> handleUserNotFound(UserNotFoundException ex)
	{
		return ResponseEntity.status(404).body(MessageDto.builder().message(ex.getMessage()).build());
	}
	
	@ExceptionHandler(UserExistsException.class)
	public ResponseEntity<MessageDto> handleUserAlreadyExists(UserExistsException ex)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageDto.builder().message(ex.getMessage()).build());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<MessageDto> handleIllegalArgument(IllegalArgumentException ex)
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageDto.builder().message(ex.getMessage()).build());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MessageDto> handleMethodArgNotValid(MethodArgumentNotValidException ex)
	{
		String errors = ex.getBindingResult().getFieldErrors().stream().map(fieldErr -> fieldErr.getDefaultMessage()).collect(Collectors.joining(", "));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageDto.builder().message(errors).build());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<MessageDto> handleInternalServerError(UserExistsException ex)
	{
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageDto.builder().message(ex.getMessage()).build());
	}
}

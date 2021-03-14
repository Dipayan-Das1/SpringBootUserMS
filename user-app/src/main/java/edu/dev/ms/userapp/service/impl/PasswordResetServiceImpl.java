package edu.dev.ms.userapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dev.ms.userapp.dto.PasswordResetDto;
import edu.dev.ms.userapp.entity.PasswordResetEntity;
import edu.dev.ms.userapp.entity.UserEntity;
import edu.dev.ms.userapp.exception.UserNotFoundException;
import edu.dev.ms.userapp.repository.PasswordResetRepository;
import edu.dev.ms.userapp.repository.UserRepository;
import edu.dev.ms.userapp.service.EmailService;
import edu.dev.ms.userapp.service.PasswordResetService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PasswordResetServiceImpl implements PasswordResetService {

	@Autowired
	private EmailService emailService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordResetRepository passwordResetRepository;
	@Autowired
	private TokenServiceImpl tokenService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional(readOnly = false)
	public boolean requestPasswordReset(String email) {
		log.info("Start password reset {}",email);
		UserEntity user = userRepository.findByEmail(email);
		if (user == null) {
			throw UserNotFoundException.userNotFoundExceptionByEmail(email);
		}
		PasswordResetEntity passwordreset = passwordResetRepository.findByEmail(email);
		String token = tokenService.generatePasswordResetToken(email);
		if(passwordreset==null)
		{
			passwordreset = new PasswordResetEntity();
			passwordreset.setEmail(email);
			passwordreset.setPasswordResetToken(token);
			passwordreset.setUser(user);
		}
		else
		{
			log.info("Password reset already exists");
			passwordreset.setPasswordResetToken(token);
		}
		
		passwordResetRepository.save(passwordreset);
		emailService.sendPasswordResetRequest(user.getFirstName(), email, token);
		log.info("Password reset request sent successfully to {}",email);
		return true;
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean resetPassword(PasswordResetDto passwordReset) {
		log.info("Reset password ");
		boolean valid = tokenService.isResetPasswordTokenVaid(passwordReset.getToken());
		if(valid)
		{
			PasswordResetEntity  passwordResetEntity = passwordResetRepository.findByPasswordResetToken(passwordReset.getToken());
			if(passwordResetEntity!=null)
			{
				UserEntity user = passwordResetEntity.getUser();
				user.setPassword(bCryptPasswordEncoder.encode(passwordReset.getPassword()));
				userRepository.save(user);
				passwordResetRepository.delete(passwordResetEntity);
				log.info("Password reset for user {}",user.getEmail());
			}
			else
			{
				throw new IllegalArgumentException("Token has expired");
			}
			
		}
		else
		{
			throw new IllegalArgumentException(String.format("Token %s has expired",passwordReset.getToken()));
		}
		return true;
	}
	
	
}

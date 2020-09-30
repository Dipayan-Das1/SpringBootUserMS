package edu.dev.ms.userapp.service;

public interface EmailService {

	boolean sendPasswordResetRequest(String firstName, String email, String token);

	void verifyEmail(String email, String verificationToken);

}

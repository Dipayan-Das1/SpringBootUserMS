package edu.dev.ms.userapp.service;

import edu.dev.ms.userapp.dto.PasswordResetDto;

public interface PasswordResetService {

	public boolean requestPasswordReset(String email);

	boolean resetPassword(PasswordResetDto passwordReset);

}

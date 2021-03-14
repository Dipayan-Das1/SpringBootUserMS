package edu.dev.ms.userapp.repository;

import org.springframework.data.repository.CrudRepository;

import edu.dev.ms.userapp.entity.PasswordResetEntity;

public interface PasswordResetRepository extends CrudRepository<PasswordResetEntity, Long> {
	PasswordResetEntity findByEmail(String email);
	PasswordResetEntity findByPasswordResetToken(String token);
}

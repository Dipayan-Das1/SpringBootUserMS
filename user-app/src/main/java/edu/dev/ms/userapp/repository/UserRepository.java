package edu.dev.ms.userapp.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.dev.ms.userapp.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByUserId(String userId);
	UserEntity findByEmail(String email);
	UserEntity findByEmailVerificationToken(String token);
}

package edu.dev.ms.userapp.bootstrap;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dev.ms.userapp.entity.AuthorityEntity;
import edu.dev.ms.userapp.entity.RoleEntity;
import edu.dev.ms.userapp.entity.UserEntity;
import edu.dev.ms.userapp.repository.AuthorityRepository;
import edu.dev.ms.userapp.repository.RoleRepository;
import edu.dev.ms.userapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserRolesLoader implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void run(String... args) throws Exception {
		AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
		AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
		AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

		RoleEntity userRole = createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
		RoleEntity adminRole = createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
		
		UserEntity userEntity = createUser(Arrays.asList(adminRole));
		log.info("User created {}",userEntity.getEmail());
		
	}

	@Transactional(readOnly = false)
	public AuthorityEntity createAuthority(String authority) {
		AuthorityEntity authorityEntity = authorityRepository.findByName(authority);
		if (authorityEntity == null) {
			authorityEntity = new AuthorityEntity();
			authorityEntity.setName(authority);
			authorityRepository.save(authorityEntity);
		}
		return authorityEntity;
	}

	@Transactional(readOnly = false)
	public RoleEntity createRole(String role, List<AuthorityEntity> authorities) {
		RoleEntity roleEntity = roleRepository.findByName(role);
		if (roleEntity == null) {
			roleEntity = new RoleEntity();
			roleEntity.setName(role);
			roleEntity.setAuthorities(authorities);
			roleRepository.save(roleEntity);
		}
		return roleEntity;
	}
	
	@Transactional(readOnly = false)
	public UserEntity createUser(List<RoleEntity> roles) {
		UserEntity userEntity = userRepository.findByEmail("iamback_dipayan@yahoo.com");
		if (userEntity == null) {
			userEntity = new UserEntity();
			userEntity.setFirstName("dipayanadmin");
			userEntity.setEmail("iamback_dipayan@yahoo.com");
			userEntity.setEmailVerificationStatus(true);
			userEntity.setUserId(UUID.randomUUID().toString());
			userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode("rootadmin"));
			userEntity.setRoles(roles);
			userRepository.save(userEntity);
		}
		return userEntity;
	}
}

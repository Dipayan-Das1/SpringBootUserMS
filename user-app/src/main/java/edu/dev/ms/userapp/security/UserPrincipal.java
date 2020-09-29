package edu.dev.ms.userapp.security;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import edu.dev.ms.userapp.entity.RoleEntity;
import edu.dev.ms.userapp.entity.UserEntity;

public class UserPrincipal implements UserDetails {
	
	private String userId;

	private UserEntity userEntity;

	public UserPrincipal(UserEntity userEntity) {
		this.userEntity = userEntity;
		this.userId = userEntity.getUserId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new LinkedList<>();

		List<RoleEntity> roles = userEntity.getRoles();
		if (roles != null) {
			roles.forEach(role -> {
				authorities.add(new SimpleGrantedAuthority(role.getName()));
				role.getAuthorities().forEach(auth -> {
					authorities.add(new SimpleGrantedAuthority(auth.getName()));
				});
			});
		}

		return authorities;
	}

	@Override
	public String getPassword() {
		return userEntity.getEncryptedPassword();
	}

	@Override
	public String getUsername() {
		return userEntity.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		// override this method
		return true;
	}

	public String getUserId() {
		return userId;
	}


	

}

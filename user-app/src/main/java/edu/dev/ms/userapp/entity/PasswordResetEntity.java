package edu.dev.ms.userapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="PASSWORD_RESET")
public class PasswordResetEntity {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false,name = "EMAIL",unique = true,length = 200)
	private String email;
	
	@Column(nullable = false,length = 200,name = "PASSWORD_RESET_TOKEN")
	private String passwordResetToken;
	
	@OneToOne
	@JoinColumn(name = "USER_ID",referencedColumnName = "ID")
	private UserEntity user;
}

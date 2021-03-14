package edu.dev.ms.userapp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "USER")
public class UserEntity {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false,name = "uuid",unique = true,length = 50)
	private String userId;
	@Column(nullable = false,name = "first_name",length = 50)
	private String firstName;
	@Column(name = "last_name",length = 50)
	private String lastName;
	@Column(nullable=false,name = "email",unique = true,length = 200)
	private String email;
	@Transient
	private String password;
	@Column(nullable = false,name = "secure_fld",length = 200)
	private String encryptedPassword;
	@Column(name = "email_token",length=200)
	private String emailVerificationToken;
	@Column(name = "verify_status")
	private Boolean emailVerificationStatus=false;	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
	private List<AddressEntity> addresses;
	
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="USERS_ROLES"
	,joinColumns = @JoinColumn(name="USER_ID",referencedColumnName = "id")
	,inverseJoinColumns = @JoinColumn(name="ROLE_ID",referencedColumnName = "id"))
	private List<RoleEntity> roles;

}

package edu.dev.ms.userapp.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name="ROLE")
@Entity
public class RoleEntity {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="NAME",nullable = false,unique = true, length = 30)
	private String name;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="ROLES_AUTHORITIES"
	,joinColumns = @JoinColumn(name="ROLE_ID",referencedColumnName = "id")
	,inverseJoinColumns = @JoinColumn(name="AUTHORITY_ID",referencedColumnName = "id"))
	private List<AuthorityEntity> authorities;
	
}

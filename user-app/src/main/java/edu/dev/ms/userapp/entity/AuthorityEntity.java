package edu.dev.ms.userapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="AUTHORITY")
public class AuthorityEntity {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="NAME",nullable = false,unique = true, length = 30)
	private String name;

}

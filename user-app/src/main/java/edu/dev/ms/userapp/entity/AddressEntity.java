package edu.dev.ms.userapp.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "ADDRESS")
@Entity
public class AddressEntity {

	@Id
	@GeneratedValue
	private Long id;
	@Column(name="ADDR_UID",unique = true,length = 50)
	private String addressId;
	@Column(name = "BUILDING_ADDR",nullable = false,length = 200)
	private String buildingAddress;
	@Column(name = "ADDRESS_LINE1",length = 200)
	private String addressLine1;
	@Column(name = "CITY",nullable = false,length = 100)
	private String city;
	@Column(name = "ZIPCODE",nullable = false,length = 7)
	private String zipcode;
	@Column(name = "COUNTRY",nullable = false,length = 25)
	private String country;
	@Column(name="TYPE",nullable = false)
	@Enumerated(EnumType.STRING)
	private AddressType addressType;
	@ManyToOne
	@JoinColumn(name="USER_ID",referencedColumnName = "id")
	private UserEntity user;
	

}

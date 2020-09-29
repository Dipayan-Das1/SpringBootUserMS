package edu.dev.ms.userapp.dto;

import edu.dev.ms.userapp.entity.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

import org.springframework.hateoas.RepresentationModel;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto extends RepresentationModel<AddressDto>{
	
	@NotEmpty(message="Bilding address cannot be blank")
	private String buildingAddress;
	private String addressLine1;
	@NotEmpty(message="City cannnot be blank")
	private String city;
	@NotEmpty(message="zipcode cannnot be blank")
	private String zipcode;
	@NotEmpty(message="countrycannot be blank")
	private String country;
	@NotEmpty(message="Address type cannot be blank")
	private AddressType addressType;
	private String addressId;
}

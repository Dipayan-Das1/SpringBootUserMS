package edu.dev.ms.userapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.dev.ms.userapp.entity.AddressEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long>{
	AddressEntity findByAddressId(String addressId);
}

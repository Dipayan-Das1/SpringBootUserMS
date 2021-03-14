package edu.dev.ms.userapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.dev.ms.userapp.entity.AuthorityEntity;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity,Long> {
	
	public AuthorityEntity findByName(String name);

}

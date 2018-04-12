package com.sport.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sport.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends CrudRepository<User, Long>{


	List<User> findByType(String type);
	Optional<User> findById(Long id);
	
	void deleteById(Long id);
	 
	List<User> findAll();
   	
	List<User> findByFirstName(String name);
}





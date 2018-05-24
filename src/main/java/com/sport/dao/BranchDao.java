package com.sport.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sport.domain.Branch;
import com.sport.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchDao extends CrudRepository<Branch, Long>{

	
	Optional<Branch> findById(Long id);
	 
	 
	void deleteById(Long id);
	 
	 
	List<Branch> findAll();

	Optional<Branch> findByBranchName(String name);
	 
}





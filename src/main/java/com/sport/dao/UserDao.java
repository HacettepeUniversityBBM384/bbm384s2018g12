package com.sport.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sport.domain.Branch;
import com.sport.domain.Course;
import com.sport.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long>{

	List<User> findByType(String type);
	
	Optional<User> findById(Long id);
	Optional<User> findByMail(String mail);
	
	List<User> findByMyCoursesC(Course c);
	 
	void deleteById(Long id);
	
	List<User> findByBranchAndType(Branch b, String type);
	 
	List<User> findAll();

	List<User> findByFirstName(String name);
	 
}





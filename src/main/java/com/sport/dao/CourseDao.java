package com.sport.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sport.domain.Course;

@Repository
public interface CourseDao extends JpaRepository<Course, Long> {

	
	Optional<Course> findById(Long id);
	Optional<Course> findByName(String name);
	 
	
	void deleteById(Long id);
	 
	 
	List<Course> findAll();

}


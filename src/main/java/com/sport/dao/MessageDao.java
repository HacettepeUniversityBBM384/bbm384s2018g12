package com.sport.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sport.domain.Branch;
import com.sport.domain.Course;
import com.sport.domain.Message;
import com.sport.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageDao extends JpaRepository<Message, Long>{

	
	Optional<Message> findById(Long id);
	
	List<Message> findByFrom(User from);
	List<Message> findByTo(User to);
	 
	void deleteById(Long id);
	
}





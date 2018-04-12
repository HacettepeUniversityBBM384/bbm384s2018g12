package com.sport.service;

import java.util.List;
import java.util.Optional;


import com.sport.domain.User;



public interface UserService {
	
	List<User> findByType(String type);
	List<User> findByName(String name);
	Optional<User> findById(Long id);
	 
	void saveUser(User usr);
	void updateUser(User usr);
	 
	void deleteUserById(Long id);
	 
	void deleteAllUsers();
	 
	List<User> findAllUsers();
	 
	boolean isUserExist(User usr);
	

}

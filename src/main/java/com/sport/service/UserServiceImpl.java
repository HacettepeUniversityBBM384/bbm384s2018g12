package com.sport.service;

import com.sport.dao.UserDao;
import com.sport.domain.User;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service("UserService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
    private UserDao userDao;
 
	
	@Override
	public List<User> findByType(String type) {
		return userDao.findByType(type);
	}
	
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public void saveUser(User user) {
    	userDao.save(user);
    }
 
    public void updateUser(User user){
        
    	saveUser(user);
    }
 
    public void deleteUserById(Long id){
    	userDao.deleteById(id);
    }
 
    public void deleteAllUsers(){
    	userDao.deleteAll();
    }
 
    public List<User> findAllUsers(){
        return (List<User>) userDao.findAll();
    }
 
    public boolean isUserExist(User user) {
        return findById(user.getId()) != null;
    }

    public List<User> findByName(String name) {
	return userDao.findByFirstName(name);
    }

   

}

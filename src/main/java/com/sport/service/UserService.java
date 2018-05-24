package com.sport.service;

import com.sport.dao.UserDao;
import com.sport.domain.Branch;
import com.sport.domain.Course;
import com.sport.domain.User;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service("userService")
@Transactional
public class UserService {

	@Autowired
    private UserDao userDao;
	
	
	public void flush() {
		userDao.flush();
	}
	
	public List<User> getByType(String type) {
		return userDao.findByType(type);
	}
	
	public Optional<User> getUser(User u) {
        return userDao.findById(u.getId());
    }
	
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public Optional<User> findByMail(String mail) {
        return userDao.findByMail(mail);
    }

    public User saveUser(User user) {
    	return userDao.save(user);
    }
    
    public void updateUser(User user){
        
    	saveUser(user);
    }

    public void deleteUserById(Long id){
    	userDao.deleteById(id);
    }


    public List<User> findAllUsers(){
        return (List<User>) userDao.findAll();
    }

    public boolean isUserExist(User user) {
        return findById(user.getId()).isPresent();
    }
    
    public boolean isUserExist(String mail) {
        return findByMail(mail).isPresent();
    }

    public List<User> getTrainers(Branch b, String type){
    	return userDao.findByBranchAndType(b, type);
    }
    
    public List<User> getUserByCourse(Course c){
    	return userDao.findByMyCoursesC(c);
    }

}

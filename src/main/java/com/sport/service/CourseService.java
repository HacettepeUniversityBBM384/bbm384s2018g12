package com.sport.service;

import com.sport.dao.CourseDao;
import com.sport.dao.UserDao;
import com.sport.domain.Branch;
import com.sport.domain.Course;
import com.sport.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service("courseService")
@Transactional
public class CourseService {

	@Autowired
    private CourseDao courseDao;
	
	
	public void flush() {
		courseDao.flush();
	}
	
    public Optional<Course> findById(Long id) {
        return courseDao.findById(id);
        
    }
    
    public Optional<Course> findByName(String name) {
        return courseDao.findByName(name);
    }
    
    public List<Course> findAll(){
    	return courseDao.findAll();
    }
    
    public Course saveCourse(Course c) {
    	return courseDao.save(c);
    }
 
    public void updateUser(Course u){
    	saveCourse(u);
    }
 
    public void deleteCourseById(Long id){
    	courseDao.deleteById(id);
    }
 
    public void deleteAllCourse(){
    	courseDao.deleteAll();
    }

    public List<String> getDefinedCourses(){
		List<Course> all = courseDao.findAll();
		List<String> n = new ArrayList<String>();
		for(Course c: all)
			if(!n.contains(c.getName())) {
				n.add(c.getName());
			}
		return n;
	}
   
    
    public List<User> getCustomers(Branch b, Course c){
    	
    	
    	
    	return null;
    }


}




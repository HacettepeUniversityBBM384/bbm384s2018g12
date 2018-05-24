package com.sport.service;

import com.sport.dao.BranchDao;
import com.sport.dao.UserDao;
import com.sport.domain.Branch;
import com.sport.domain.Course;
import com.sport.domain.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service("branchService")
@Transactional
public class BranchService {

	@Autowired
    private BranchDao branchDao;
	
	public void flush() {
		branchDao.flush();
	}
	
	public Optional<Branch> findById(Long id) {
        return branchDao.findById(id);
    }
    
    public Optional<Branch> findByBranchName(String name) {
        return branchDao.findByBranchName(name);
    }

    public void saveBranch(Branch br) {
    	branchDao.save(br);
    }
 
    public void updateBranch(Branch br){
        
    	saveBranch(br);
    }
 
    public void deleteBranchById(Long id){
    	branchDao.deleteById(id);

    }
    
    public void deleteAllBranch(){
    	branchDao.deleteAll();
    }
 
    public List<Branch> findAllBranchs(){
        return (List<Branch>) branchDao.findAll();
    }
 
    public boolean isBranchExist(Branch br) {
        return findById(br.getId()) != null;
    }
    
    public void getOpenCourses(Branch b) {
    	
    }
    

}

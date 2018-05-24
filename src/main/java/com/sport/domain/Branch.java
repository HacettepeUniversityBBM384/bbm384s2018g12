package com.sport.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.springframework.transaction.annotation.Transactional;

@Entity
public class Branch {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String branchName;
    private String location;


    @OneToOne
    private User branchManager;

    
    @OneToMany
    public List<User> trainers;
    
    @OneToMany
    public List<Course> openCourses;
    
    
    
    
    public List<Course> getOpenCourses() {
		return openCourses;
	}


	public void setOpenCourses(List<Course> openCourses) {
		this.openCourses = openCourses;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getBranchName() {
		return branchName;
	}


	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public User getBranchManager() {
		return branchManager;
	}


	public void setBranchManager(User branchManager) {
		this.branchManager = branchManager;
	}


    
}














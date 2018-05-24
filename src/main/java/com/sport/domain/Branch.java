package com.sport.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
    private List<Course> openCourses;


    @OneToMany
    private List<User> trainers;


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


	public List<Course> getOpenCourses() {
		return openCourses;
	}


	public void setOpenCourses(List<Course> openCourses) {
		this.openCourses = openCourses;
	}


	public List<User> getTrainers() {
		return trainers;
	}


	public void setTrainers(List<User> trainers) {
		this.trainers = trainers;
	}
    
    
}














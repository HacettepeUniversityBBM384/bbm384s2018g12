package com.sport.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class User {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;

	    private String firstName;
	    private String lastName;
	    private String password;
	    private String mail;
	    private String type;
	    
	    
	    @OneToMany(fetch=FetchType.EAGER)
	    public List<Course> myCoursesT; //for trainers
	    
	    
	    @OneToMany(fetch=FetchType.EAGER)
	    @Fetch(value = FetchMode.SUBSELECT)
	    public List<Course> myCoursesC; //for customer


	    @OneToOne
	    public Branch branch;


		public long getId() {
	        return id;
	    }

	    public void setId(long id) {
	        this.id = id;
	    }

	    public String getFirstName() {
	        return firstName;
	    }

	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getMail() {
	        return mail;
	    }

	    public void setMail(String mail) {
	        this.mail = mail;
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }

	}

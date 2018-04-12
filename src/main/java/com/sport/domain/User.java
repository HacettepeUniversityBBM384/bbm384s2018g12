package com.sport.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

	    @Override
	    public String toString() {
	        return "Student{" +
	                "id=" + id +
	                ", firstName='" + firstName + '\'' +
	                ", lastName='" + lastName + '\'' +
	                ", password='" + password + '\'' +
	                ", mail='" + mail + '\'' +
	                '}';
	    }
	}

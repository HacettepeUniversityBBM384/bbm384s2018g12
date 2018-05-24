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
public class Message {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;
	 	
	 	
	 	@OneToOne
	    private User from;
	    
	 	@OneToOne
	 	private User to;

	    private String subject;

	    private String message;

	    
		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public User getFrom() {
			return from;
		}

		public void setFrom(User from) {
			this.from = from;
		}

		public User getTo() {
			return to;
		}

		public void setTo(User to) {
			this.to = to;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public long getId() {
			return id;
		}

	    
	}

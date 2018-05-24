package com.sport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private MailSender mailSender;
	
	public void sendSimpleMessage(String from, String to, String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(content);
        message.setTo(to);
        message.setFrom(from);
        mailSender.send(message);
    }
	
	
}

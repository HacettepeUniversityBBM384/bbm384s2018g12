package com.sport.service;

import com.sport.dao.MessageDao;
import com.sport.dao.UserDao;
import com.sport.domain.Branch;
import com.sport.domain.Course;
import com.sport.domain.Message;
import com.sport.domain.User;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service("messageService")
@Transactional
public class MessageService {

	@Autowired
    private MessageDao messageDao;
	
	
	public void flush() {
		messageDao.flush();
	}
	
	
    public Optional<Message> findById(Long id) {
        return messageDao.findById(id);
    }

    public List<Message> findByFrom(User from) {
        return messageDao.findByFrom(from);
    }
    
    public List<Message> findByTo(User to) {
        return messageDao.findByTo(to);
    }

    public Message saveMessage(Message m) {
    	return messageDao.save(m);
    }
    
    public void deleteMessageById(Long id){
    	messageDao.deleteById(id);
    }


}

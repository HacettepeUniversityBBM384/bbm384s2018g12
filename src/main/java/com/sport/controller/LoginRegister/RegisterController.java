package com.sport.controller.LoginRegister;

import java.security.MessageDigest;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.domain.User;
import com.sport.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

	
	@Autowired
	private UserService userService;
	

	@RequestMapping(value="", method = RequestMethod.GET)
	public String showRegistrationPage(Model model, User user){
		model.addAttribute("user", user);
		return "register";
	}


	@RequestMapping(value = "", method = RequestMethod.POST)
	public String process(Model model, @RequestParam String repassword, @Valid User user, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "register";
		}
		if(isFieldEmpty(user, repassword)) {
			model.addAttribute("errorMessage", " Fields cannot be empty!");
			return "register";
		}
		
		Optional<User> cust = userService.findByMail(user.getMail());
		if(cust.isPresent()) {
			model.addAttribute("errorMessage", " The user already exists!");
			return "register";
		}
		if(!user.getPassword().equals(repassword))
			model.addAttribute("errorMessage", " Passwords don't match!");
		else {
			createNewUser(user);
			model.addAttribute("success", " Registering is successfull..");
		}
		
		return "register";
	}
	
	
	private void createNewUser(User cust) {
		//cust.setPassword(UUID.randomUUID().toString());
		cust.setType("customer");
		userService.saveUser(cust);
	}
	
	private boolean isFieldEmpty(User cust, String repassword) {
		if(cust.getFirstName().equals(""))
			return true;
		if(cust.getLastName().equals(""))
			return true;
		if(cust.getMail().equals(""))
			return true;
		if(cust.getPassword().equals(""))
			return true;
		if(repassword.equals(""))
			return true;
		return false;
	}
	
}










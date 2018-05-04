package com.sport.controller.LoginRegister;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.domain.User;
import com.sport.service.UserService;


@Controller
@RequestMapping("")
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("")
	public String index(HttpSession session) {
	
		User usr = (User) session.getAttribute("user");
		
		if(usr==null)
            return "redirect:/login";
        return "redirect:/" + usr.getType();
	}

	@RequestMapping(value = "/login", method= RequestMethod.GET)
    public String login(ModelMap modelMap, HttpSession session) {
		User usr = (User) session.getAttribute("user");
        if(usr!=null)
            return "redirect:/" + usr.getType();
        return "login";
    }
	
	
	@RequestMapping(value = "/login", method=RequestMethod.POST)
    public String login(@RequestParam String mail, @RequestParam String password, Model model, HttpSession session) {
        
		
		Optional<User> user = userService.findByMail(mail);
        
        if(user.isPresent()) {
        	User usr = user.get();
            if(usr.getPassword().equals(password)) {
                session.setAttribute("user", usr);
                return "redirect:/" + usr.getType();
            }
        }
        if(mail.equals("") || password.equals(""))
        	model.addAttribute("error", " Please fill the blanks!");
        else
        	model.addAttribute("error", " Email or password incorrect!");
        return "login";
    }



}

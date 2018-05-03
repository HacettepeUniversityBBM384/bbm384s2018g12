package com.sport.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sport.domain.User;
import com.sport.service.CourseService;
import com.sport.service.UserService;

@Controller
@RequestMapping("/customer")
public class CustomerController {


	@Autowired
	private UserService userService;

	@Autowired
	private CourseService courseService;


	@RequestMapping("")
	public String index(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		return "homeCustomer";
	}

}











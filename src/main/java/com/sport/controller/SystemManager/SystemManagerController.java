package com.sport.controller.SystemManager;


import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sport.domain.User;
import com.sport.service.EmailService;
import com.sport.service.UserServiceImpl;


@Controller
@RequestMapping("/system-manager")
public class SystemManagerController {
	

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private EmailService emailService;
	
	
	@RequestMapping(value="", method = RequestMethod.GET)
    public String system_manager(Model model) {


		model.addAttribute("employees", getAllEmployees());

        return "manipulate";
    }


	@RequestMapping(value="add_employee",  method = RequestMethod.GET)
    public String add_employee_get(@Valid @ModelAttribute("employee") User employee, Model model) {
		
		model.addAttribute("employee", new User());
		
		return "add_employee";
	}


	@RequestMapping(value="add_employee",  method = RequestMethod.POST)
    public String add_employee_post(@Valid @ModelAttribute("employee") User employee, Model model) {
		
		if(employee.getType().equals("trainer"))
			employee.setType("Trainer");
		else if(employee.getType().equals("system_manager"))
			employee.setType("System Manager");
		else if(employee.getType().equals("branch_manager"))
			employee.setType("Branch Manager");
		employee.setPassword(UUID.randomUUID().toString().substring(0, 10).replace("-", ""));
    	
		String from = "bene-fit@benefit.com";
		String to = employee.getMail();
		String subject = "Bene-fit registration";
		String content = "Dear " + employee.getFirstName() + " " + employee.getLastName() + ",\n"
				    + "We accepted your application for. You can log in to the system information below.\n"
				    + "Email: " + employee.getMail() + "\n"
				    + "Password: " + employee.getPassword() + "\n"
				    + "Sincerely.";

		emailService.sendSimpleMessage(from, to, subject, content);
		
		userServiceImpl.saveUser(employee);
		return "add_employee";
	}


	@RequestMapping(value = "/update/{id}")
    public String update_employee(@PathVariable long id, Model model, HttpSession session) {
		User emp = userServiceImpl.findById(id).get();
		
		model.addAttribute("employee", emp);
		
    	return "update_employee";
    }


	@RequestMapping(value="", method = RequestMethod.POST)
    public String update_employee(@Valid @ModelAttribute("employee") User employee, Model model) {
		
		long id = employee.getId();
		String type = employee.getType();

		
		if(type.equals("trainer"))
			type="Trainer";
		else if(type.equals("system_manager"))
			type="System Manager";
		else if(type.equals("branch_manager"))
			type="Branch Manager";


		User emp = userServiceImpl.findById(id).get();
		emp.setType(type);
		
		model.addAttribute("employees", getAllEmployees());

		return "manipulate";
    }


	@RequestMapping(value = "/delete/{id}")
    public String delete_employee(@PathVariable long id, Model model, HttpSession session) {
		
		userServiceImpl.deleteUserById(id);
		
    	return "redirect:/system-manager";
    }


	private List<User> getAllEmployees() {
		
		List<User> trainers = userServiceImpl.findByType("Trainer");
		List<User> employees = userServiceImpl.findByType("Branch Manager");
		List<User> sys_man = userServiceImpl.findByType("System Manager");
		
		employees.addAll(trainers);
		employees.addAll(sys_man);
		
		return employees;
	}
	
	
}









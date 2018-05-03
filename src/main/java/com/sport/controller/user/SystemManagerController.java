package com.sport.controller.user;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sport.domain.Branch;
import com.sport.domain.Course;
import com.sport.domain.User;
import com.sport.service.BranchService;
import com.sport.service.CourseService;
import com.sport.service.EmailService;
import com.sport.service.UserService;


@Controller
@RequestMapping("/system-manager")
public class SystemManagerController {
	

	@Autowired
	private UserService userService;
	
	@Autowired
	private BranchService branchService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private EmailService emailService;

	
	/*list courses*/
	@RequestMapping(value="/courses")
    public String show_courses(Model model) {
		model.addAttribute("courses", courseService.findAll());
        return "SM_list_course";
    }
	
	@RequestMapping(value = "/courses/delete/{id}")
    public String deleteCourse(@PathVariable long id, Model model, HttpSession session) {
		
		courseService.deleteCourseById(id);
		
    	return "redirect:/system-manager/courses";
    }
	
	/*list branch*/
	
	@RequestMapping(value="/branches")
    public String show_branchs(Model model) {

		model.addAttribute("branches", branchService.findAllBranchs());

        return "SM_list_branch";
    }
	
	@RequestMapping(value = "/branches/delete/{id}")
    public String deleteBranch(@PathVariable long id, Model model, HttpSession session) {
		
		branchService.deleteBranchById(id);
		
    	return "redirect:/system-manager/branches";
    }
	
	/*define course*/
	@RequestMapping("define_course")
    public String defineCourse(Model model, HttpSession session, @Valid @ModelAttribute("branch") Course course) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		
		User usr = (User) session.getAttribute("user");
		
		model.addAttribute(new Course());
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
        return "SM_define_course";
    }
	

	@RequestMapping(value="/define_course",  method = RequestMethod.POST)
    public String defineCoursePost(@Valid @ModelAttribute("course") Course course, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());


		Optional<Course> c = courseService.findByName(course.getName());

		if(c.isPresent()) {
			model.addAttribute("error", " Course is already exists!");
			return "SM_define_course";
		}else {
			course.setOpen(false);
			courseService.saveCourse(course);
			model.addAttribute("success", course.getName() + " has been defined successfully!");
		}
		
        return "SM_define_course";
    }
	

	/*define branch*/
	@RequestMapping("define_branch")
    public String defineBranch(Model model, HttpSession session, @Valid @ModelAttribute("branch") Branch branch) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		
		User usr = (User) session.getAttribute("user");
		
		model.addAttribute(new Branch());
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
        return "SM_define_branch";
    }


	@RequestMapping(value="/define_branch",  method = RequestMethod.POST)
    public String defineBranchPost(@Valid @ModelAttribute("branch") Branch branch, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());


		Optional<Branch> br = branchService.findByBranchName(branch.getBranchName());

		if(br.isPresent()) { /*checks if branch exists.*/
			model.addAttribute("error", " Branch is already exists!");
			return "SM_define_branch";
		}else {
			branchService.saveBranch(branch);
			model.addAttribute("success", branch.getBranchName() + " has been defined successfully!");
		}
		
		
		
        return "SM_define_branch";
    }
	
	
	
	@RequestMapping("")
    public String index(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
        return "homeSystemManager";
    }

	
	
	/**/
	
	@RequestMapping(value="/manipulate")
    public String system_manager(Model model) {

		model.addAttribute("employees", getAllEmployees());

        return "manipulate";
    }

	

	@RequestMapping(value="/manipulate/add_employee",  method = RequestMethod.GET)
    public String add_employee_get(@Valid @ModelAttribute("employee") User employee, Model model) {
		
		model.addAttribute("employee", new User());
		
		return "add_employee";
	}


	@RequestMapping(value="/manipulate/add_employee",  method = RequestMethod.POST)
    public String add_employee_post(@Valid @ModelAttribute("employee") User employee, Model model) {

		employee.setPassword(UUID.randomUUID().toString().substring(0, 10).replace("-", ""));
    	
		String from = "bene-fit@benefit.com";
		String to = employee.getMail();
		String subject = "Bene-fit registration";
		String content = "Dear " + employee.getFirstName() + " " + employee.getLastName() + ",\n"
				    + "We accepted your application. You can log in to the system information below.\n"
				    + "Email: " + employee.getMail() + "\n"
				    + "Password: " + employee.getPassword() + "\n"
				    + "Sincerely.";

		emailService.sendSimpleMessage(from, to, subject, content);
		
		userService.saveUser(employee);
		//Barkin redirected to system manager page
		return "redirect:/system-manager/manipulate";
	}


	@RequestMapping(value = "/manipulate/update/{id}")
    public String update_employee(@PathVariable long id, Model model, HttpSession session) {
		User emp = userService.findById(id).get();
		
		model.addAttribute("employee", emp);
		
    	return "update_employee";
    }


	@RequestMapping(value="/manipulate", method = RequestMethod.POST)
    public String update_employee(@Valid @ModelAttribute("employee") User employee, Model model) {
		
		long id = employee.getId();
		String type = employee.getType();
		

		User emp = userService.findById(id).get();
		emp.setType(type);
		emp.setFirstName(employee.getFirstName());
		emp.setLastName(employee.getLastName());
		
		model.addAttribute("employees", getAllEmployees());
		return "manipulate";
    }


	@RequestMapping(value = "/manipulate/delete/{id}")
    public String delete_employee(@PathVariable long id, Model model, HttpSession session) {
		
		userService.deleteUserById(id);
		
    	return "redirect:/system-manager/manipulate";
    }


	private List<User> getAllEmployees() {
		
		List<User> trainers = userService.findByType("trainer");
		List<User> employees = userService.findByType("branch-manager");
		//List<User> sys_man = userService.findByType("system-manager");
		
		employees.addAll(trainers);
		//employees.addAll(sys_man);
		
		return employees;
	}
	
	
}









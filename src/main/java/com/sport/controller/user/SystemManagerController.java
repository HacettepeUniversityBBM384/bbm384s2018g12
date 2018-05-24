package com.sport.controller.user;


import java.util.ArrayList;
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
import com.sport.domain.Message;
import com.sport.domain.User;
import com.sport.service.BranchService;
import com.sport.service.CourseService;
import com.sport.service.EmailService;
import com.sport.service.MessageService;
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
	
	@Autowired
	private MessageService messageService;



	@RequestMapping("")
    public String index(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
        return "homeSystemManager";
    }

	
	/*list courses*/
	@RequestMapping(value="/courses")
    public String show_courses(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
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
    public String show_branchs(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		model.addAttribute("branches", branchService.findAllBranchs());

        return "SM_list_branch";
    }
	
	@RequestMapping(value = "/branches/delete/{id}")
    public String deleteBranch(@PathVariable long id, Model model, HttpSession session) {
		
		Branch b = branchService.findById(id).get();
		User u = b.getBranchManager();
		b.setBranchManager(null);
		u.branch = null;
		branchService.deleteBranchById(id);
		
    	return "redirect:/system-manager/branches";
    }
	
	
	/*list employees*/
	@RequestMapping(value="/employees")
    public String show_employees(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		model.addAttribute("employees", getAllEmployees());

        return "SM_list_employee";
    }
	
	@RequestMapping(value = "/employees/delete/{id}")
    public String deleteEmployee(@PathVariable long id, Model model, HttpSession session) {
		
		userService.deleteUserById(id);
		
    	return "redirect:/system-manager/employees";
    }
	

	/*define course*/
	@RequestMapping("define_course")
    public String defineCourse(Model model, HttpSession session, @Valid @ModelAttribute("branch") Course course) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
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
		model.addAttribute("user", usr);

		

		Optional<Course> c = courseService.findByName(course.getName());

		if(c.isPresent()) {
			model.addAttribute("error", " Course is already exists!");
			return "SM_define_course";
		}else {
			
			courseService.saveCourse(course);
			model.addAttribute("success", course.getName() + " has been defined successfully!");
		}
		
        return "SM_define_course";
    }



	/*define branch*/
	@RequestMapping("define_branch")
    public String defineBranch(Model model, HttpSession session,
    		@Valid @ModelAttribute("branch") Branch branch,
    		@ModelAttribute("branch_manager") User bm) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		model.addAttribute(new Branch());
		model.addAttribute("branchManagers", getFreeBranchManagers());		
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
        return "SM_define_branch";
    }


	@RequestMapping(value="/define_branch",  method = RequestMethod.POST)
    public String defineBranchPost(@Valid @ModelAttribute("branch") Branch branch, 
    							   @ModelAttribute("branch_manager") User bm,
    							   Model model, HttpSession session) {
		
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		

		Optional<Branch> br = branchService.findByBranchName(branch.getBranchName());

		if(br.isPresent()) {
			model.addAttribute("error", " Branch is already exists!");
			return "SM_define_branch";
		}else {
			User bmm = userService.findById(bm.getId()).get();
			Branch b = new Branch();
			b.setBranchManager(bmm);
			b.setLocation(branch.getLocation());
			b.setBranchName(branch.getBranchName());
			branchService.saveBranch(b);
			
			bmm.branch = b;
			model.addAttribute("success", branch.getBranchName() + " has been defined successfully!");
		}
		
		model.addAttribute("branchManagers", getFreeBranchManagers());	
		
        return "SM_define_branch";
    }
	
	
	
	/*manipulating*/
	
	@RequestMapping(value="/manipulate")
    public String system_manager(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		model.addAttribute("employees", getAllEmployees());

        return "manipulate";
    }

	

	@RequestMapping(value="/manipulate/add_employee",  method = RequestMethod.GET)
    public String add_employee_get(@Valid @ModelAttribute("employee") User employee, Model model, HttpSession session) {
		
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		model.addAttribute("employee", new User());
		
		return "add_employee";
	}


	@RequestMapping(value="/manipulate/add_employee",  method = RequestMethod.POST)
    public String add_employee_post(@Valid @ModelAttribute("employee") User employee, Model model, HttpSession session) {
		System.err.println("-" + employee.getMail() + "-");
		if(employee.getMail().trim().equals("")){
			if(session.getAttribute("user")==null)
	            return "redirect:/login";
			User usr = (User) session.getAttribute("user");
			model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
			model.addAttribute("user", usr);
			
			model.addAttribute("employee", new User());
			model.addAttribute("errorMessage", " Please enter an email!");
			return "add_employee";
		}
		else if(userService.isUserExist(employee.getMail())) {
			if(session.getAttribute("user")==null)
	            return "redirect:/login";
			User usr = (User) session.getAttribute("user");
			model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
			model.addAttribute("user", usr);

			model.addAttribute("employee", new User());
			model.addAttribute("errorMessage", " The user already exists!");
			return "add_employee";
		}
		
		
		
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
		flush();
		return "redirect:/system-manager/manipulate";
	}


	@RequestMapping(value = "/manipulate/update/{id}")
    public String update_employee(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		User emp = userService.findById(id).get();
		
		model.addAttribute("employee", emp);
		
    	return "update_employee";
    }


	@RequestMapping(value="/manipulate", method = RequestMethod.POST)
    public String update_employee(@Valid @ModelAttribute("employee") User employee, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		User emp = userService.findById(employee.getId()).get();
		emp.setType(employee.getType());
		emp.setFirstName(employee.getFirstName());
		emp.setLastName(employee.getLastName());
		emp.setPassword(employee.getPassword());
		
		model.addAttribute("employees", getAllEmployees());
		return "manipulate";
    }


	@RequestMapping(value = "/manipulate/delete/{id}")
    public String delete_employee(@PathVariable long id, Model model, HttpSession session) {
		
		User t = userService.findById(id).get();
		
		if(t.getType().equals("trainer")) {
			removeCoursesOfATrainer(t);
			t.branch.trainers.remove(t);
		}
		userService.deleteUserById(id);
		
    	return "redirect:/system-manager/manipulate";
    }
	
	/*sending messages*/
	@RequestMapping("/send-message")
	public String sendMessage(@ModelAttribute("msg") Message msg, @ModelAttribute("trainer") User trainer, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());

		model.addAttribute("users", userService.findAllUsers());
		
		return "SM_send_message";
	}
	
	
	@RequestMapping(value= "/send-message", method=RequestMethod.POST)
	public String sendMessagePost(@ModelAttribute("msg") Message msg, @ModelAttribute("trainer") User trainer, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser(((User) session.getAttribute("user"))).get();

		User t = userService.findById(trainer.getId()).get();
		
		msg.setSubject(msg.getSubject());
		msg.setFrom(usr);
		msg.setTo(t);
		messageService.saveMessage(msg);

		return "redirect:/system-manager/send-message";
	}
	/******/


	/*messagebox*/
	@RequestMapping("/messagebox")
	public String messagebox(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser((User) session.getAttribute("user")).get();
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		
		List<Message> m = messageService.findByTo(usr);
		
		model.addAttribute("messages", m);
		return "SM_messagebox";
	}

	@RequestMapping("/messages/{id}")
	public String messagebox(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser((User) session.getAttribute("user")).get();
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());

		model.addAttribute("msg", messageService.findById(id).get());
		return "SM_read_message";
	}
	
	@RequestMapping("/messages/delete/{id}")
	public String deleteMessage(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser((User) session.getAttribute("user")).get();
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		
		messageService.deleteMessageById(id);
		
		return "redirect:/system-manager/messagebox";
	}
	
	private List<User> getAllEmployees() {

		List<User> trainers = userService.getByType("trainer");
		List<User> employees = userService.getByType("branch-manager");

		employees.addAll(trainers);

		return employees;
	}
	
	private List<User> getFreeBranchManagers(){
		System.err.println("a");
		List<User> bm = userService.getByType("branch-manager");
		System.err.println("b");
		
		List<User> nbm = new ArrayList<User>();
		System.err.println("c");
		
		for(User u: bm)
			if(u.branch==null)
				nbm.add(u);
		return nbm;
	}
	
	/* removes course from the users which taken the course */
	public void removeOneCourseFromCustomers(Course cc) {
		List<User> custs = userService.getUserByCourse(cc);
		for(User u: custs) {
			User uu = userService.findById(u.getId()).get();
			uu.myCoursesC.remove(cc);
		}

		courseService.deleteCourseById(cc.getId());

	}


	/* when a trainer's courses will be removed from branch */
	public void removeCoursesOfATrainer(User trainer) {
		Branch b = branchService.findById(trainer.branch.getId()).get();
		
		for(Course c: trainer.myCoursesT) {
			Course cc = courseService.findById(c.getId()).get();

			removeOneCourseFromCustomers(cc);
			b.openCourses.remove(cc);
		}
	}

	public void flush() {
		courseService.flush();
		branchService.flush();
		userService.flush();
		messageService.flush();
	}

}









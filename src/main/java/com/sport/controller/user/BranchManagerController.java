package com.sport.controller.user;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sport.dao.CourseDao;
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
@RequestMapping("/branch-manager")
public class BranchManagerController {
	
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
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
        return "BM_home";
    }
	

	/******manipulating courses****/
	@RequestMapping(value="/manipulate-course", method=RequestMethod.GET)
    public String system_manager(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser(((User) session.getAttribute("user"))).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		Branch b = branchService.findById(usr.branch.getId()).get();
		List<Course> openCourses = b.getOpenCourses();//branchService.getOpenCourse(usr.branch);
		model.addAttribute("courses", openCourses);

        return "BM_manipulate_course";
    }

	@RequestMapping(value = "/manipulate-course/update/{id}")
    public String update_course(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser(((User) session.getAttribute("user"))).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		Course c = courseService.findById(id).get();
		
		model.addAttribute("course", c);
		
    	return "BM_update_course";
    }

	@RequestMapping(value = "/manipulate-course/update", method=RequestMethod.POST)
    public String update_course(@RequestParam long id, @Valid @ModelAttribute("course") Course course, Model model, HttpSession session) {

		System.err.println(id);
		System.err.println(course.getName());
		
		Course cc = courseService.findById(id).get();
		
		
		cc.setFee(course.getFee());
		cc.setName(course.getName());
		cc.setQuota(course.getQuota());
		
		flush();
		return "redirect:/branch-manager/manipulate-course";
    }


	@RequestMapping("/open-course")
    public String openCourse(@ModelAttribute("course") Course course, 
    						 @ModelAttribute("trainer") User trainer,
    						 Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		Branch b = branchService.findById(usr.branch.getId()).get();
		List<String> definedCourses = courseService.getDefinedCourses();
		List<User> trainers = userService.getTrainers(b, "trainer");
		
		model.addAttribute("definedCourses", definedCourses);
		model.addAttribute("trainers", trainers);
		
		return "BM_open_course";
	}


	@RequestMapping(value="/open-course", method=RequestMethod.POST)
    public String openCoursePost(@ModelAttribute("course") Course p_course,
    							 @ModelAttribute("trainer") User p_trainer,
    							 @RequestParam int quota,
    							 @RequestParam int fee,
    							 Model model, HttpSession session) {

		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		User t = userService.findById(p_trainer.getId()).get();
		
		for(Course c: t.myCoursesT)
			if(c.getName().equals(p_course.getName())) {
				Branch b = branchService.findById(usr.branch.getId()).get();
				List<String> definedCourses = courseService.getDefinedCourses();
				List<User> trainers = userService.getTrainers(b, "trainer");
				
				model.addAttribute("definedCourses", definedCourses);
				model.addAttribute("trainers", trainers);
				model.addAttribute("error", "Trainer already has the course!");
				return "BM_open_course";
			}
		
		
		p_course.setQuota(quota);
		p_course.setFee(fee);
		p_course.setTrainer(t);
		Course c = courseService.saveCourse(p_course);

		Branch b = branchService.findById(usr.branch.getId()).get();

		b.openCourses.add(c);
		t.myCoursesT.add(c);
		
		
		flush();
		
		return "redirect:/branch-manager/manipulate-course";
	}
	
	@RequestMapping(value = "/manipulate-course/delete/{id}")
    public String close_course(@PathVariable long id, Model model, HttpSession session) {
		User usr = (User) session.getAttribute("user");
		Course c = courseService.findById(id).get();
		User t = c.getTrainer();
		
		Branch b = branchService.findById(usr.branch.getId()).get();
		
		t.myCoursesT.remove(c);  // remove course from trainer
		b.getOpenCourses().remove(c);  // remove course from open course
		
		removeOneCourseFromCustomers(c);
		
		
		flush();
		
    	return "redirect:/branch-manager/manipulate-course";
    }
	
	
	/*manipulating trainer and customer*/
	@RequestMapping(value="/manipulate")
    public String manipulateTrainer(HttpSession session, Model model) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		model.addAttribute("trainers", userService.getTrainers(usr.branch, "trainer"));

        return "BM_manipulate";
    }



	@RequestMapping(value="/manipulate/add_employee",  method = RequestMethod.GET)
    public String add_trainer_get(@Valid @ModelAttribute("employee") User employee, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		model.addAttribute("trainer", new User());

		return "BM_add_employee";
	}


	@RequestMapping(value="/manipulate/add_employee",  method = RequestMethod.POST)
    public String add_trainer_post(@Valid @ModelAttribute("employee") User employee, Model model, HttpSession session) {

		employee.setPassword(UUID.randomUUID().toString().substring(0, 10).replace("-", ""));
    	
		String from = "bene-fit@benefit.com";
		String to = employee.getMail();
		String subject = "Bene-fit registration";
		String content = "Dear " + employee.getFirstName() + " " + employee.getLastName() + ",\n"
				    + "We accepted your application. You can log in to the system information below.\n"
				    + "Email: " + employee.getMail() + "\n"
				    + "Password: " + employee.getPassword() + "\n"
				    + "Sincerely.";

		//emailService.sendSimpleMessage(from, to, subject, content);
		
		User bm = (User) session.getAttribute("user");
		
		User trainer = userService.saveUser(employee);
		Branch b = branchService.findById(bm.branch.getId()).get();
		trainer.branch = b;
		b.trainers.add(trainer);
		flush();
		return "redirect:/branch-manager/manipulate";
	}


	@RequestMapping(value = "/manipulate/update/{id}")
    public String update_trainer(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		User emp = userService.findById(id).get();
		
		model.addAttribute("trainer", emp);
		
    	return "BM_update_trainer";
    }

	@RequestMapping(value = "/manipulate/update", method=RequestMethod.POST)
    public String update_trainer(@Valid @ModelAttribute("employee") User employee, Model model, HttpSession session) {
		
		User emp = userService.findById(employee.getId()).get();
		emp.setFirstName(employee.getFirstName());
		emp.setLastName(employee.getLastName());
		emp.setPassword(employee.getPassword());
		
		flush();
		return "redirect:/branch-manager/manipulate";
    }


	@RequestMapping(value = "/manipulate/delete/{id}")
    public String delete_employee(@PathVariable long id, Model model, HttpSession session) {
		
		User bm = (User) session.getAttribute("user");
		
		Branch b = branchService.findById(bm.branch.getId()).get();
		User t = userService.findById(id).get();
		
		b.trainers.remove(t);
		
		removeCoursesOfATrainer(t);
		
		
		userService.deleteUserById(id);
		
		flush();
    	return "redirect:/branch-manager/manipulate";
    }

	
	/*sending messages*/
	@RequestMapping("/send-message")
	public String sendMessage(@ModelAttribute("msg") Message msg, @ModelAttribute("user") User user, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser(((User) session.getAttribute("user"))).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		List<User> users = userService.findAllUsers();
		
		
		model.addAttribute("users", userService.findAllUsers());
		return "BM_send_message";
	}
	
	
	@RequestMapping(value= "/send-message", method=RequestMethod.POST)
	public String sendMessagePost(@ModelAttribute("msg") Message msg, @ModelAttribute("user") User user, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser(((User) session.getAttribute("user"))).get();

		User t = userService.findById(user.getId()).get();
		
		msg.setSubject(msg.getSubject());
		msg.setFrom(usr);
		msg.setTo(t);
		messageService.saveMessage(msg);

		return "redirect:/branch-manager/send-message";
	}
	
	@RequestMapping("/messages/{id}")
	public String messagebox(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser((User) session.getAttribute("user")).get();
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		
		
		model.addAttribute("msg", messageService.findById(id).get());
		return "BM_read_message";
	}
	
	@RequestMapping("/messages/delete/{id}")
	public String deleteMessage(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser((User) session.getAttribute("user")).get();
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		
		messageService.deleteMessageById(id);
		
		return "redirect:/branch-manager/messagebox";
	}
	
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
		return "BM_messagebox";
	}
	
	private List<User> getTrainerAndCustomer() {
		
		List<User> trainers = userService.getByType("trainer");
		List<User> employees = userService.getByType("branch-manager");
		
		employees.addAll(trainers);
		
		return employees;
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





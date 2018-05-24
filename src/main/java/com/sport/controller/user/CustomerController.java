package com.sport.controller.user;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
@RequestMapping("/customer")
public class CustomerController {


	@Autowired
	private UserService userService;
	
	@Autowired
	private BranchService branchService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private EmailService emailService;


	@RequestMapping("")
	public String index(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		return "C_home";
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
		return "C_messagebox";
	}


	/*evaluating*/
	@RequestMapping("/evaluate-trainer")
	public String evaluateTrainer(@ModelAttribute("trainer") User trainer, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());

		List<User> users = userService.getByType("trainer");
		model.addAttribute("users", users);
		
		return "C_evaluate_trainer";
	}


	@RequestMapping(value="/evaluate-trainer", method=RequestMethod.POST)
	public String evaluateTrainerPost(@ModelAttribute("trainer") User trainer, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());

		List<User> users = userService.getByType("trainer");
		model.addAttribute("users", users);
		model.addAttribute("message", "Evaluating is successfull!");
		
		return "C_evaluate_trainer";
	}
	
	@RequestMapping("/evaluate-course")
	public String evaluateCourse(@ModelAttribute("course") Course course, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());

		List<Course> courses = courseService.findAll();
		model.addAttribute("courses", courses);
		
		return "C_evaluate_course";
	}


	@RequestMapping(value="/evaluate-course", method=RequestMethod.POST)
	public String evaluateCoursePost(@ModelAttribute("course") Course course, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser((User) session.getAttribute("user")).get();
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());

		List<Course> courses = courseService.findAll();
		model.addAttribute("courses", courses);
		model.addAttribute("message", "Evaluating is successfull!");
		
		return "C_evaluate_course";
	}


	/*sending messages*/
	@RequestMapping("/send-message")
	public String sendMessage(@ModelAttribute("msg") Message msg, @ModelAttribute("trainer") User trainer, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());

		List<User> users = userService.getByType("trainer");
		
		
		model.addAttribute("users", userService.findAllUsers());
		return "C_send_message";
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

		return "redirect:/customer/send-message";
	}
	
	/*messagebox*/
	@RequestMapping("/messages/{id}")
	public String messagebox(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser((User) session.getAttribute("user")).get();
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		
		
		model.addAttribute("msg", messageService.findById(id).get());
		return "C_read_message";
	}

	@RequestMapping("/messages/delete/{id}")
	public String deleteMessage(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser((User) session.getAttribute("user")).get();
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		
		messageService.deleteMessageById(id);
		
		return "redirect:/customer/messagebox";
	}

	@RequestMapping(value="/my-courses")
	public String lstMyCourse(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.findById(((User) session.getAttribute("user")).getId()).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		List<Course> courses = usr.myCoursesC;
		
		model.addAttribute("courses", courses);
		return "C_list_my_course";
	}
	
	@RequestMapping(value="/drop-course/{id}")
	public String dropCourse(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.findById(((User) session.getAttribute("user")).getId()).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		Course c = courseService.findById(id).get();
		User t = userService.findById(usr.getId()).get();
		
		t.myCoursesC.remove(c);
		flush();
		return "redirect:/customer/my-courses";
	}
	
	@RequestMapping(value="/list-courses")
	public String lstOpenCourses(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		List<Branch> allB = branchService.findAllBranchs();
		
		
		model.addAttribute("branchs", allB);
		
		return "C_list_course";
	}
	
	@RequestMapping(value="/branch/{id}")
	public String lstOpenCourses(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = (User) session.getAttribute("user");
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		List<Branch> allB = branchService.findAllBranchs();
		Branch b = branchService.findById(id).get();
		List<Course> courses = getCourses(b, usr);
		
		model.addAttribute("branchs", allB);
		model.addAttribute("courses", courses);
		model.addAttribute("branch", b);
		model.addAttribute("me", usr);
		
		return "C_list_course";
	}
	
	
	@RequestMapping(value="/sign/{c_id}")
	public String payment(@PathVariable long c_id, Model model, HttpSession session) {
		
		return "redirect:/customer/payment/"+ c_id;
	}

		
	
	
	/***** payment ******/
	@RequestMapping(value="/payment/{c_id}")
	public String sign(@PathVariable long c_id, Model model, HttpSession session) {

		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser((User) session.getAttribute("user")).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());	
		model.addAttribute("user", usr);
		
		System.err.println(usr.getId());
		Course c = courseService.findById(c_id).get();
		usr.myCoursesC.add(c);
		flush();
		model.addAttribute("course", c);
		return "C_payment";
	}

	@RequestMapping(value="/payment")
	public String signPost(Model model, HttpSession session) {
		return "redirect:/customer/my-courses";
	}
	
	
	
	public List<Course> getCourses(Branch b, User u){
		List<Course> courses = b.openCourses;
		User uu = userService.findById(u.getId()).get();
		
		for(Course c: uu.myCoursesC)
			if(courses.contains(c))
				courses.remove(c);
		return courses;
	}


	public void flush() {
		courseService.flush();
		branchService.flush();
		userService.flush();
		messageService.flush();
	}
}











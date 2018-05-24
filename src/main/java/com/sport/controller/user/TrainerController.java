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
@RequestMapping("/trainer")
public class TrainerController {


	@Autowired
	private UserService userService;
	
	@Autowired
	private BranchService branchService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private MessageService messageService;
	


	@RequestMapping("")
	public String index(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser(((User) session.getAttribute("user"))).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		return "T_home";
	}
	

	/*sending messages*/
	@RequestMapping("/send-message")
	public String sendMessage(@ModelAttribute("msg") Message msg, @ModelAttribute("trainer") User trainer, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser(((User) session.getAttribute("user"))).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);

		model.addAttribute("users", userService.findAllUsers());
		return "T_send_message";
	}
	
	
	@RequestMapping(value= "/send-message", method=RequestMethod.POST)
	public String sendMessagePost(@ModelAttribute("msg") Message msg, @ModelAttribute("trainer") User trainer, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser(((User) session.getAttribute("user"))).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		User t = userService.findById(trainer.getId()).get();
		
		msg.setSubject(msg.getSubject());
		msg.setFrom(usr);
		msg.setTo(t);
		messageService.saveMessage(msg);

		return "redirect:/trainer/send-message";
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
		return "T_messagebox";
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
		return "T_read_message";
	}
	
	
	@RequestMapping("/messages/delete/{id}")
	public String deleteMessage(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser((User) session.getAttribute("user")).get();
		model.addAttribute("user", usr);
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		
		messageService.deleteMessageById(id);
		
		return "redirect:/trainer/messagebox";
	}
	
	@RequestMapping(value="/my-courses")
	public String lstMyCourse(Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser(((User) session.getAttribute("user"))).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		List<Course> courses = usr.myCoursesT;
		
		model.addAttribute("courses", courses);
		return "T_list_my_course";
	}
	
	@RequestMapping(value="/drop-course/{id}")
	public String dropCourse(@PathVariable long id, Model model, HttpSession session) {
		if(session.getAttribute("user")==null)
            return "redirect:/login";
		User usr = userService.getUser(((User) session.getAttribute("user"))).get();
		model.addAttribute("username", usr.getFirstName() + " " + usr.getLastName());
		model.addAttribute("user", usr);
		
		Course c = courseService.findById(id).get();
		User t = userService.findById(usr.getId()).get();
		
		Branch b = branchService.findById(usr.branch.getId()).get();
		
		b.openCourses.remove(c);
		t.myCoursesT.remove(c);
		
		removeOneCourseFromCustomers(c);
		
		flush();
		return "redirect:/trainer/my-courses";
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
		for(Course c: trainer.myCoursesT) {
			Course cc = courseService.findById(c.getId()).get();
			
			removeOneCourseFromCustomers(cc);
		}
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











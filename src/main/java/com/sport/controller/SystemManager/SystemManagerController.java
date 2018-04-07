package com.sport.controller.SystemManager;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/system-manager")
public class SystemManagerController {
	
	
	@RequestMapping(value="", method = RequestMethod.GET)
    public String system_manager(Model model) {


		
        return "manipulate";
    }


	
	
	
}









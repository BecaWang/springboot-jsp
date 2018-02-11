package main.java.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	//http://localhost:8080/employeeForm

	@RequestMapping("/employeeForm")
	public String employeeForm() {
		return "employeeForm";
	}
}

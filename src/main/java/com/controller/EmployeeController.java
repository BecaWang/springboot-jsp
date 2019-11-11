package main.java.com.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import main.java.com.model.Employee;
import main.java.com.model.EmployeeJsonRespone;

@RestController
public class EmployeeController {
		   
/**
 * 	 複習一下rest post
 * url:http://localhost:8080/saveEmployee
 *   {
			"firstName":"wang",
			"lastName":"ann",
			"email":"ok@gmail.com",
			"designation":"taipei"
		
	}
	*/
	   @PostMapping(value = "/saveEmployee", produces = "application/json")
	   public EmployeeJsonRespone saveEmployee(@RequestBody  Employee employee) {
		   
	      EmployeeJsonRespone respone = new EmployeeJsonRespone();
	      respone.setEmployee(employee);
	      return respone;
	   }

}

package main.java.com.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import main.java.com.model.Employee;
import main.java.com.model.EmployeeJsonRespone;

@RestController
public class EmployeeController {
	
	   @PostMapping(value = "/saveEmployee", produces = { MediaType.APPLICATION_JSON_VALUE })
	   public EmployeeJsonRespone saveEmployee(@ModelAttribute  Employee employee) {

	      EmployeeJsonRespone respone = new EmployeeJsonRespone();
	      respone.setEmployee(employee);
	      return respone;
	   }

}

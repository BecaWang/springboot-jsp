package test.local.controller;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import main.java.com.model.Employee;
import main.java.com.model.EmployeeJsonRespone;

public class EmployeeControllerTest {

	@Test
	public void test(){
		
		 RestTemplate restTemplate = new RestTemplate();
		 
		 Employee req = new Employee();
		 req.setFirstName("wang");
		 req.setLastName("ann");
		 req.setEmail("ok@gmail.com");
		 req.setDesignation("taipei");
		 
		 String uri = "http://localhost:8080/saveEmployee";
		 EmployeeJsonRespone result = restTemplate.postForObject(uri, req, EmployeeJsonRespone.class);
		 System.out.println(result.getEmployee().getEmail());
	}
}

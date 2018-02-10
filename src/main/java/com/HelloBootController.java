package main.java.com;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloBootController {
	
	//http://localhost:8080/
	
	@RequestMapping("/")
	public String helloPage(){
		return "Hello! Spring Boot and maven";
	}
	
	// http://localhost:8080/hello/QQ
	 @RequestMapping("/hello/{name}")
	  public @ResponseBody
	  String hello(@PathVariable("name") String name) {
	   return "Hello " + name;
	  }
}

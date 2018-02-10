package main.java.com;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShowMessageController {

	@Value("${show.message}")
	private String message = "Hello QQ";

	//http://localhost:8080/show

	
	@RequestMapping("/show")
	public String show(Map<String, Object> model) {
		model.put("message", this.message);
		return "showMessage";
	}
	
}

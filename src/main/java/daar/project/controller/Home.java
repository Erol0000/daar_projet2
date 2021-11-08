package daar.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {
	
	@GetMapping("/")
	public String home() {
		return "home :)";
	}	
	
	@GetMapping("/addResume")
	public String addResume() {
		return "addResume :)";
	}	
	
	@GetMapping("/removeResume")
	public String removeResume() {
		return "removeResume :)";
	}
	
	@GetMapping("/allResume")
	public String allResume() {
		return "allResume :)";
	}
	
	@GetMapping("/searchResume")
	public String searchResume() {
		return "searchResume :)";
	}
}
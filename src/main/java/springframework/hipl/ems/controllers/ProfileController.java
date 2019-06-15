package springframework.hipl.ems.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.services.EmployeeService;

@Controller
public class ProfileController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value = "/profile")
	public String showProfile(Model model, HttpSession session) {
		String username = (String) session.getAttribute("username");
		if(username==null) {
			return "redirect:login";
		}
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		for (Employee employee : employeeList) {
			model.addAttribute("employee",employee);
		}
		return "profile";
	}
}

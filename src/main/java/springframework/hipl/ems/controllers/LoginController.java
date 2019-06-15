package springframework.hipl.ems.controllers;

import java.util.List;
//import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.services.EmployeeService;

@Controller
@SessionAttributes("userSession")
public class LoginController {
	
	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(value="/login")
	public String getLogin() {
		return "login";
	}
	
	@RequestMapping(value="/loginCheck", method = RequestMethod.POST)
	public String loginCheck(@ModelAttribute Employee employee,
								@RequestParam("username") String username,
								@RequestParam("password") String password,
								Model model,
								HttpSession session) {
		model.addAttribute("userSession",username);
		List<Employee> employeeList = employeeService.getEmployeeByUsernameAndPassword(username, password);
		if(employeeList.isEmpty()) {
			model.addAttribute("message","Invalid Username or Password");
			return "login";
		}
		for(Employee emp : employeeList) {
			model.addAttribute("employee",emp);
		}
		session.setAttribute("username", username);
		model.addAttribute("message", "Successful");
		return "redirect:dailyRegister";
	}
}

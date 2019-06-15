package springframework.hipl.ems.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import springframework.hipl.ems.bootstraps.EmployeeBootstrap;
import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.services.EmployeeService;

@Controller
public class RegisterController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value="/employeeRegister")
	public String getEmployeeRegister() {
		return "employee-register";
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(@ModelAttribute Employee employee,
						@RequestParam("firstName") String firstName,
						@RequestParam("lastName") String lastName,
						@RequestParam("dateOfBirth") String dateOfBirth,
						@RequestParam("gender") String gender,
						@RequestParam("address") String address,
						@RequestParam("emailId") String emailId,
						@RequestParam("phoneNo") String phoneNo,
						@RequestParam("username") String username,
						@RequestParam("password") String password,
						@RequestParam("userType") String userType,
						Model model,
						HttpSession session) {
		String uname = (String) session.getAttribute("username");
		if(uname == null) {
			return "redirect:login";
		}
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		for (Employee employee2 : employeeList) {
			model.addAttribute("employee", employee2);
		}
		EmployeeBootstrap employeeBootstrap = new EmployeeBootstrap(employeeService);
		String message = employeeBootstrap.registerEmployee(firstName, lastName, dateOfBirth, gender, address, emailId, phoneNo, username, password,userType);
		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);
		model.addAttribute("dateOfBirth", dateOfBirth);
		model.addAttribute("gender",gender);
		model.addAttribute("address", address);
		model.addAttribute("emailId", emailId);
		model.addAttribute("phoneNo", phoneNo);
		model.addAttribute("username", username);
		model.addAttribute("password", password);
		model.addAttribute("userType",userType);
		model.addAttribute("message", message);
		return "register-successful";
	}
}

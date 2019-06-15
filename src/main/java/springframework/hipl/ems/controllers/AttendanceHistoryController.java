package springframework.hipl.ems.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import springframework.hipl.ems.models.Attendance;
import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.services.AttendanceService;
import springframework.hipl.ems.services.EmployeeService;

@Controller
public class AttendanceHistoryController {
	
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value = "/attendanceHistory")
	public String showAttendanceHistory(HttpSession session, Model model) {
		String username = (String) session.getAttribute("userSession");
		if(username == null) {
			return "redirect:login";
		}
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendanceList = attendanceService.getEmployeeAttendance(employeeList);
		model.addAttribute("attendances", attendanceList);
		for(Employee employee : employeeList) {
			model.addAttribute("employee",employee);
		}
		return "attendance-history";
	}
}

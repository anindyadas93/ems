package springframework.hipl.ems.controllers;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import springframework.hipl.ems.models.Attendance;
import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.services.AttendanceService;
import springframework.hipl.ems.services.EmployeeService;

@Controller
public class ShowAttendanceController {
	
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private EmployeeService employeeService;
	
	LocalDateTime localDateTime = LocalDateTime.now();
	String currentDate = localDateTime.getDayOfMonth()+"/"+localDateTime.getMonthValue()+"/"+localDateTime.getYear();
	
	@RequestMapping(value = "/showAllAttendances")
	public String showAttendance(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		for (Employee employee : employeeList) {
			model.addAttribute("employee",employee);
		}
//		List<Attendance> attendances = attendanceService.getAllAttendnace();
//		model.addAttribute("value","Attendance of All Employees");
//		model.addAttribute("attendances",attendances);
		return "attendance-show";
	}
	
	@RequestMapping(value = "/showAttendanceWorkingDate")
	public String showAttendanceWorkingDate(@RequestParam("workingDate") String workingDate,Model model,HttpSession session) {
		String username = (String) session.getAttribute("username");
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		for (Employee employee : employeeList) {
			model.addAttribute("employee", employee);
		}
		String date = workingDate.substring(8, workingDate.length())+"/"+workingDate.substring(6, 7)+"/"+workingDate.substring(0, 4);
		List<Attendance> atteendanceList = attendanceService.getAttendanceByWorkingDate(date);
		model.addAttribute("attendances", atteendanceList);
//		for (Attendance attendances : atteendanceList) {
//			model.addAttribute("attendances", attendances);
//		}
		return "attendance-show";
	}
	
	@RequestMapping(value = "/showAttendance")
	public String showEmployeeAttendance(HttpSession session,Model model) {
		String username = (String) session.getAttribute("username");
		if(username == null) {
			return "redirect:login";
		}
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendances = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		model.addAttribute("value","Attendance of "+username);
		model.addAttribute("attendances",attendances);
		return "attendance-show";
	}
	
	@RequestMapping(value = "/searchAttendance", method = RequestMethod.POST)
	public String searchAttendance(	@RequestParam("employeeName") String employeeName,
									@RequestParam("month") String month,
									HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		for (Employee employee : employeeList) {
			model.addAttribute("employee",employee);
		}
		String m1 = "__"+month+"_____";
		String m2 = "___"+month+"_____";
		List<Employee> employeeList2 = employeeService.getEmployeeByUsername(employeeName);
		List<Attendance> attendanceList = attendanceService.getAttendanceByEmployeeAndWorkingDateLikeOrWorkingDateLike(employeeList2, m1, m2);
		model.addAttribute("attendances", attendanceList);
		model.addAttribute("employeeName", employeeName);
		model.addAttribute("select", "selected");
		return "attendance-show";
	}
}

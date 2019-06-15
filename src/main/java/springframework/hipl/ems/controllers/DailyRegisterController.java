package springframework.hipl.ems.controllers;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import springframework.hipl.ems.bootstraps.BreakBootstrap;
import springframework.hipl.ems.models.Attendance;
import springframework.hipl.ems.models.Break;
import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.services.AttendanceService;
import springframework.hipl.ems.services.BreakService;
import springframework.hipl.ems.services.EmployeeService;

@Controller
public class DailyRegisterController {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private BreakService breakService;
	@Autowired
	JdbcTemplate jdbc;
	
	LocalDateTime localDateTime = LocalDateTime.now();
	String currentDate = localDateTime.getDayOfMonth()+"/"+localDateTime.getMonthValue()+"/"+localDateTime.getYear();
	
	@RequestMapping(value = "/dailyRegister")
	public String getDailyRegister(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");
		if(username==null) {
			return "redirect:login";
		}
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendances = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		model.addAttribute("value","Attendance of "+username);
		model.addAttribute("attendances",attendances);
		
		BreakBootstrap breakBootstrap = new BreakBootstrap(employeeService, attendanceService, breakService, jdbc);
		List<Break> breakList = breakBootstrap.getEmployeeBreaks(username);
		model.addAttribute("breaks",breakList);
		
		String totalBreakTime = breakBootstrap.getTotalBreakTime(username);
		model.addAttribute("totalBreakTime", totalBreakTime);
		
		for (Employee employee : employeeList) {
			model.addAttribute("employee", employee);
		}
		
		return "daily-register";
	}
	
}

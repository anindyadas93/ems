package springframework.hipl.ems.controllers;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springframework.hipl.ems.bootstraps.AttendanceBootstrap;
import springframework.hipl.ems.models.Attendance;
import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.services.AttendanceService;
import springframework.hipl.ems.services.EmployeeService;

@Controller
public class AttendanceRegisterController {
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	JdbcTemplate jdbc;
	
	LocalDateTime localDateTime = LocalDateTime.now();
	String currentDate = localDateTime.getDayOfMonth()+"/"+localDateTime.getMonthValue()+"/"+localDateTime.getYear();
	
	@RequestMapping(value = "/attendanceIn")
	public String registerAttendance(HttpSession session) {
		String username = (String) session.getAttribute("username");
		AttendanceBootstrap attendanceBootstrap = new AttendanceBootstrap(employeeService, attendanceService, jdbc);
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendanceList = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		if(attendanceList.isEmpty()) {
			attendanceBootstrap.setInTime(username);
		}
		return "redirect:dailyRegister";
	}
	
	@RequestMapping(value = "/attendanceOut")
	public String registerOutTimeAttendance(HttpSession session) {
		String username = (String) session.getAttribute("username");
		AttendanceBootstrap attendanceBootstrap = new AttendanceBootstrap(employeeService, attendanceService, jdbc);
		attendanceBootstrap.setOutTime(username);
		return "redirect:calculateTotalTime";
	}
	
	@RequestMapping(value = "/calculateTotalTime")
	public String calculateTotalTime(HttpSession session) {
		String username = (String) session.getAttribute("username");
		AttendanceBootstrap attendanceBootstrap = new AttendanceBootstrap(employeeService, attendanceService, jdbc);
		attendanceBootstrap.calculateTotalTime(username);
		return "redirect:calculateWorkingTime";
	}
	
	@RequestMapping(value = "/calculateWorkingTime")
	public String calculateWorkingTime(HttpSession session) {
		String username = (String) session.getAttribute("username");
		AttendanceBootstrap attendanceBootstrap = new AttendanceBootstrap(employeeService, attendanceService, jdbc);
		attendanceBootstrap.calculateWorkingTime(username);
		return "redirect:dailyRegister";
	}
}

package springframework.hipl.ems.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import springframework.hipl.ems.repositories.AttendanceRepository;
import springframework.hipl.ems.repositories.EmployeeRepository;

@Controller
public class LogoutController {
	
	@Autowired
	AttendanceRepository attendanceRepository;
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	JdbcTemplate jdbc;
	
	@RequestMapping(value = "/logout")
	public String logoutUser(HttpSession httpSession, SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		httpSession.invalidate();;
		return "redirect:login";
	}
}
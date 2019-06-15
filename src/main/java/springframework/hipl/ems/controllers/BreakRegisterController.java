package springframework.hipl.ems.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import springframework.hipl.ems.bootstraps.BreakBootstrap;
import springframework.hipl.ems.services.AttendanceService;
import springframework.hipl.ems.services.BreakService;
import springframework.hipl.ems.services.EmployeeService;

@Controller
public class BreakRegisterController {
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private BreakService breakService;
	@Autowired
	JdbcTemplate jdbc;
	
	@RequestMapping(value = "/breakStart")
	public String startBreak(HttpSession session) {
		String username = (String) session.getAttribute("username");
		if(username==null) {
			return "redirect:login";
		}
		BreakBootstrap breakBootstrap = new BreakBootstrap(employeeService, attendanceService, breakService, jdbc);
		breakBootstrap.setBreak(username);
		return "redirect:dailyRegister";
	}
	
	@RequestMapping(value = "/breakEnd")
	public String breakEnd(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");
		if(username==null) {
			return "redirect:login";
		}		
		BreakBootstrap breakBootstrap = new BreakBootstrap(employeeService, attendanceService, breakService, jdbc);
		breakBootstrap.setBreakEnd(username);
		return "redirect:breakCalculate";
	}
	
	@RequestMapping(value = "/breakCalculate")
	public String calculateBreak(HttpSession session) {
		String username = (String) session.getAttribute("username");
		BreakBootstrap breakBootstrap = new BreakBootstrap(employeeService, attendanceService, breakService, jdbc);
		breakBootstrap.getTotalBreakTime(username);
		return "redirect:dailyRegister";
	}
	
}

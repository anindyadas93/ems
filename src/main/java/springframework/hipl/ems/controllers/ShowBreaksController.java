package springframework.hipl.ems.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import springframework.hipl.ems.bootstraps.BreakBootstrap;
import springframework.hipl.ems.models.Break;
import springframework.hipl.ems.services.AttendanceService;
import springframework.hipl.ems.services.BreakService;
import springframework.hipl.ems.services.EmployeeService;

@Controller
@SessionAttributes("userSession")
public class ShowBreaksController {
	
	/*
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private BreakRepository breakRepository;
	@Autowired
	private AttendanceRepository attendanceRepository;
	*/
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private BreakService breakService;
	@Autowired
	JdbcTemplate jdbc;
	
	@RequestMapping(value = "/showBreaks")
	public String getShowBreaks(@ModelAttribute("userSession") String username,
								Model model) {
		BreakBootstrap breakBootstrap = new BreakBootstrap(employeeService, attendanceService, breakService, jdbc);
		List<Break> breakList = breakBootstrap.getEmployeeBreaks(username);
		model.addAttribute("breaks",breakList);
		return "break-register";
	}
}

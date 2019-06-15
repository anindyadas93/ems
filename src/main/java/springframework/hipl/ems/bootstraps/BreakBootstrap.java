package springframework.hipl.ems.bootstraps;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import springframework.hipl.ems.models.Attendance;
import springframework.hipl.ems.models.Break;
import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.services.AttendanceService;
import springframework.hipl.ems.services.BreakService;
import springframework.hipl.ems.services.EmployeeService;

public class BreakBootstrap {
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private BreakService breakService;
	@Autowired
	JdbcTemplate jdbc;

	LocalDateTime localDateTime = LocalDateTime.now();
	String currentTime = localDateTime.getHour()+":"+localDateTime.getMinute();
	String currentDate = localDateTime.getDayOfMonth()+"/"+localDateTime.getMonthValue()+"/"+localDateTime.getYear();
	
	public BreakBootstrap(EmployeeService employeeService, AttendanceService attendanceService, BreakService breakService, JdbcTemplate jdbc) {
		this.employeeService = employeeService;
		this.attendanceService = attendanceService;
		this.breakService = breakService;
		this.jdbc = jdbc;
	}

	public void setBreak(String username) {
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendanceList = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		List<Break> breeakList = breakService.getBreakByEmployeeAndAttendance(employeeList, attendanceList);
		String inTime = null;
		String outTime = null;
		for (Attendance attendance : attendanceList) {
			outTime = attendance.getOutTime();
			inTime = attendance.getInTime();
		}
		if(inTime == null || outTime == null) {
			int status = 0;
			for (Break break1 : breeakList) {
				status = break1.getStatus();
			}
			if(status==0) {
				List<Break> breakList = new ArrayList<Break>();
				Break b = new Break();
				b.setBreakStart(currentTime);
				b.setStatus(1);
				b.setEmployees(employeeList);
				b.setAttendances(attendanceList);
				breakList.add(b);
				breakService.saveBreak(breakList);
			}
		}
		else {
			System.out.println("out time is not null");
		}
//		int status = 0;
//		for (Break break1 : breeakList) {
//			status = break1.getStatus();
//		}
//		if(status==0) {
//			List<Break> breakList = new ArrayList<Break>();
//			Break b = new Break();
//			b.setBreakStart(currentTime);
//			b.setStatus(1);
//			b.setEmployees(employeeList);
//			b.setAttendances(attendanceList);
//			breakList.add(b);
//			breakService.saveBreak(breakList);
//		}
	}
	
	public void setBreakEnd(String username) {
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendanceList = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		List<Break> breakList = breakService.getBreakByEmployeeAndAttendanceAndStatus(employeeList, attendanceList, 1);
		String outTime = null;
		for (Attendance attendance : attendanceList) {
			outTime = attendance.getOutTime();
		}
		if(outTime == null) {
			if(!breakList.isEmpty()) {
				int breakId = breakList.iterator().next().getId();
				String breakStart = breakList.iterator().next().getBreakStart();
				String breakEnd = currentTime;
				String breakTime = null;
				jdbc.update("UPDATE break SET break_end='"+currentTime+"' WHERE break_id='"+breakId+"' AND break_end IS NULL");
				breakTime = calculateBreak(breakStart, breakEnd);
				jdbc.update("UPDATE break SET break_time='"+breakTime+"',status=0 WHERE break_id='"+breakId+"' AND status=1");
			}
		}
		else {
			System.out.println("outtime is not null");
		}
//		if(!attendanceList.isEmpty()) {
//			
//		}
//		if(!breakList.isEmpty()) {
//			int breakId = breakList.iterator().next().getId();
//			String breakStart = breakList.iterator().next().getBreakStart();
//			String breakEnd = currentTime;
//			String breakTime = null;
//			jdbc.update("UPDATE break SET break_end='"+currentTime+"' WHERE break_id='"+breakId+"' AND break_end IS NULL");
//			breakTime = calculateBreak(breakStart, breakEnd);
//			jdbc.update("UPDATE break SET break_time='"+breakTime+"',status=0 WHERE break_id='"+breakId+"' AND status=1");
//		}
	}
	
	public List<Break> getEmployeeBreaks(String username){
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendanceList = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		List<Break> breakList = breakService.getBreakByEmployeeAndAttendance(employeeList, attendanceList);
		return breakList;
	}
	
	public String calculateBreak(String inTime, String outTime){
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		Date d1 = null,d2 = null;
		try {
			d1 = formatter.parse(inTime);
			d2 = formatter.parse(outTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long hr, min;
        Long diff = (d2.getTime() - d1.getTime()) / 1000;
        hr = diff / 60 / 60;
        min = (diff / 60) % 60;
        String breakTime = hr.toString()+":"+min.toString();
        return breakTime;
	}
	
	public String getTotalBreakTime(String username) {
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendanceList = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		List<Break> breakList = breakService.getBreakByEmployeeAndAttendanceAndStatus(employeeList, attendanceList, 0);
		String breakTime = null;
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		Date d1 = null, d2 = null;
		Long totalBreakTime = (long) 0;
		for (Break break1 : breakList) {
			try {
				breakTime = break1.getBreakTime();
				d1 = formatter.parse(breakTime);
				d2 = formatter.parse("00:00");
				totalBreakTime += (d1.getTime() - d2.getTime())/1000;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		Long hr, min;
        hr = totalBreakTime / 60 / 60;
        min = (totalBreakTime / 60) % 60;
        breakTime = hr.toString()+":"+min.toString();
        int attendanceId = 0;
        for (Attendance attendance : attendanceList) {
        	if(attendance.getTotalTime() == null) {
        		attendanceId = attendance.getId();
        	}
		}
        jdbc.update("UPDATE attendance SET break_time='"+breakTime+"' WHERE attendance_id='"+attendanceId+"'");
		return breakTime;
	}
	
}

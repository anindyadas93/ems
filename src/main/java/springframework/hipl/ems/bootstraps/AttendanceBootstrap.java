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
import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.services.AttendanceService;
import springframework.hipl.ems.services.EmployeeService;

public class AttendanceBootstrap {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	JdbcTemplate jdbc;
		
	LocalDateTime localDateTime = LocalDateTime.now();
	String currentDate = localDateTime.getDayOfMonth()+"/"+localDateTime.getMonthValue()+"/"+localDateTime.getYear();
	String currentTime = localDateTime.getHour()+":"+localDateTime.getMinute();
	
	public AttendanceBootstrap(EmployeeService employeeService,	AttendanceService attendanceService, JdbcTemplate jdbc) {
		this.employeeService = employeeService;
		this.attendanceService = attendanceService;
		this.jdbc = jdbc;
	}

	public void setInTime(String username) {
		List<Employee> employees = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendances = new ArrayList<Attendance>();
		Attendance a = new Attendance();
		a.setInTime(currentTime);
		a.setWorkingDate(currentDate);
		a.setEmployees(employees);
		a.setEmpId(employees.iterator().next().getId());
		attendances.add(a);
		attendanceService.saveAttendance(attendances);
	}
	
	public void setOutTime(String username) {
		String currentTime = localDateTime.getHour()+":"+localDateTime.getMinute();
		String currentDate = localDateTime.getDayOfMonth()+"/"+localDateTime.getMonthValue()+"/"+localDateTime.getYear();
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendanceList = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		for (Attendance attendance : attendanceList) {
			if(attendance.getOutTime()==null) {
				int attendanceId = attendanceList.iterator().next().getId();
				jdbc.update("UPDATE attendance SET out_time='"+currentTime+"' WHERE attendance_id='"+attendanceId+"'");
			}
		}
	}
	
	public void calculateTotalTime(String username) {
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendanceList = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		String totalTime = null;
		int attendanceId = 0;
		String start = null;
		String end = null;
		for (Attendance attendance : attendanceList) {
			if(attendance.getWorkingTime() == null) {
				attendanceId = attendance.getId();
				start = attendance.getInTime();
				end = attendance.getOutTime();
				totalTime = calculateTime(start,end);
			}
		}
		jdbc.update("UPDATE attendance SET total_time='"+totalTime+"' WHERE attendance_id='"+attendanceId+"'");
	}
	
	public void calculateWorkingTime(String username) {
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendanceList = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		String workingTime = null;
		int attendanceId = 0;
		String start = null;
		String end = null;
		for (Attendance attendance : attendanceList) {
			if(attendance.getWorkingTime() == null) {
				attendanceId = attendance.getId();
				start = attendance.getBreakTime();
				end = attendance.getTotalTime();
				workingTime = calculateTime(start, end);
			}
		}
		jdbc.update("UPDATE attendance SET working_time='"+workingTime+"' WHERE attendance_id='"+attendanceId+"'");
	}
	
	public String calculateTime(String start, String end){
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		Date d1 = null,d2 = null;
		try {
			d1 = formatter.parse(start);
			d2 = formatter.parse(end);
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
	
	public boolean checkInTime(String username) {
		String currentDate = localDateTime.getDayOfMonth()+"/"+localDateTime.getMonthValue()+"/"+localDateTime.getYear();
		List<Employee> employeeList = employeeService.getEmployeeByUsername(username);
		List<Attendance> attendanceList = attendanceService.getAttendanceByWorkingDateAndEmployee(currentDate, employeeList);
		for (Attendance attendance : attendanceList) {
			if(attendance.getInTime().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
}
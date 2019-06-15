package springframework.hipl.ems.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springframework.hipl.ems.models.Attendance;
import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.repositories.AttendanceRepository;

@Service
public class AttendanceService {
	
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	public List<Attendance> getAllAttendnace() {
		List<Attendance> attendances = new ArrayList<>();
		attendanceRepository.findAll().iterator().forEachRemaining(attendances::add);
		return attendances;
	}
	
	public List<Attendance> getEmployeeAttendance(List<Employee> employees){
		List<Attendance> attendances = new ArrayList<>();
		attendanceRepository.findByEmployees(employees).iterator().forEachRemaining(attendances::add);
		return attendances;
	}
	
	public List<Attendance> getAttendanceByWorkingDateAndEmployee(String workingDate, List<Employee> employeeList){
		List<Attendance> attendanceList = new ArrayList<>();
		attendanceRepository.findByWorkingDateAndEmployees(workingDate, employeeList).iterator().forEachRemaining(attendanceList::add);
		return attendanceList;
	}
	
	public List<Attendance> getAttendanceByEmployeeAndWorkingDateLikeOrWorkingDateLike(List<Employee> employeeList, String month1, String month2){
		List<Attendance> attendanceList1 = new ArrayList<>();
		attendanceRepository.findByEmployeesAndWorkingDateLikeOrWorkingDateLike(employeeList, month1, month2).iterator().forEachRemaining(attendanceList1::add);;
		return attendanceList1;
	}
	
	public List<Attendance> getAttendanceByWorkingDate(String workingDate){
		System.out.println(workingDate);
		List<Attendance> attendanceList = new ArrayList<>();
		attendanceRepository.findByWorkingDate(workingDate).iterator().forEachRemaining(attendanceList::add);
		return attendanceList;
	}
	
	public void saveAttendance(List<Attendance> attendanceList) {
		attendanceRepository.saveAll(attendanceList);
	}
}

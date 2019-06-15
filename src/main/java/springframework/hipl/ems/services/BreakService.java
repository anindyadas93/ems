package springframework.hipl.ems.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springframework.hipl.ems.models.Attendance;
import springframework.hipl.ems.models.Break;
import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.repositories.BreakRepository;

@Service
public class BreakService {
	
	@Autowired
	private BreakRepository breakRepository;
	
	public List<Break> getBreakByEmployee(List<Employee> employeeList){
		List<Break> breakList = new ArrayList<Break>();
		breakRepository.findByEmployees(employeeList).iterator().forEachRemaining(breakList::add);;
		return breakList;
	}
	
	public List<Break> getBreakByEmployeeAndAttendance(List<Employee> employeeList, List<Attendance> attendanceList){
		List<Break> breakList = new ArrayList<>();
		breakRepository.findByEmployeesAndAttendances(employeeList, attendanceList).iterator().forEachRemaining(breakList::add);
		return breakList;
	}
	
	public List<Break> getBreakByEmployeeAndAttendanceAndStatus(List<Employee> employeeList, List<Attendance> attendanceList, int status){
		List<Break> breakList = new ArrayList<>();
		breakRepository.findByEmployeesAndAttendancesAndStatus(employeeList, attendanceList, status).iterator().forEachRemaining(breakList::add);
		return breakList;
	}
	
	public void saveBreak(List<Break> breakList) {
		breakRepository.saveAll(breakList);
	}
	
}

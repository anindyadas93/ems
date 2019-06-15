package springframework.hipl.ems.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springframework.hipl.ems.models.Attendance;
import springframework.hipl.ems.models.Break;
import springframework.hipl.ems.models.Employee;

@Repository
public interface BreakRepository extends JpaRepository<Break, Integer>{
	
	List<Break> findByEmployees(List<Employee> employeeList);
	
	List<Break> findByEmployeesAndAttendances(List<Employee> employeeList, List<Attendance> attendanceList);
	
	Set<Break> findByEmployeesAndAttendancesAndStatus(List<Employee> employeeList, List<Attendance> attendanceList,int status);
	
}

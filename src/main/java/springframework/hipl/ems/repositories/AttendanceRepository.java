package springframework.hipl.ems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springframework.hipl.ems.models.Attendance;
import springframework.hipl.ems.models.Employee;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{
	
	List<Attendance> findAll();
	
	List<Attendance> findByEmployees(List<Employee> employees);
	
	List<Attendance> findByWorkingDateAndEmployees(String workingDate, List<Employee> employeeList);
	
	List<Attendance> findByEmployeesAndWorkingDateLikeOrWorkingDateLike(List<Employee> employeeList, String month1, String month2);
	
	List<Attendance> findByWorkingDate(String workingDate);
	
}

package springframework.hipl.ems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springframework.hipl.ems.models.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
	List<Employee> findByUsername(String username);
	
	List<Employee> findByUsernameAndPassword(String username, String password);
	
	List<Employee> findAll();
}

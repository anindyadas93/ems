package springframework.hipl.ems.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.repositories.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public List<Employee> getEmployeeByUsername(String username){
		return employeeRepository.findByUsername(username);
	}
	
	public List<Employee> getEmployeeByUsernameAndPassword(String username, String password){
		return employeeRepository.findByUsernameAndPassword(username, password);
	}
	
	public List<Employee> getAllEmployee(){
		return employeeRepository.findAll();
	}
	
	public void saveEmployee(List<Employee> employeeList) {
		employeeRepository.saveAll(employeeList);
	}
	
}

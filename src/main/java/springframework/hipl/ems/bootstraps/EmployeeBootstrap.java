package springframework.hipl.ems.bootstraps;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import springframework.hipl.ems.models.Employee;
import springframework.hipl.ems.services.EmployeeService;

public class EmployeeBootstrap {
	
	@Autowired
	private EmployeeService employeeService;

	public EmployeeBootstrap(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public String registerEmployee(String firstName,String lastName,String dateOfBirth,String gender,String address,String emailId,String phoneNo,String username,String password,String userType) {
		String message = null;
		List<Employee> employees = employeeService.getEmployeeByUsername(username);
		if(employees.isEmpty()){
			List<Employee> employeeList = new ArrayList<Employee>();
			Employee emp = new Employee();
			emp.setFirstName(firstName);
			emp.setLastName(lastName);
			emp.setDateOfBirth(dateOfBirth);
			emp.setGender(gender);
			emp.setAddress(address);
			emp.setEmailId(emailId);
			emp.setPhoneNo(phoneNo);
			emp.setUsername(username);
			emp.setPassword(password);
			emp.setUserType(userType);
			employeeList.add(emp);
			employeeService.saveEmployee(employeeList);
			message="Successfully Registered";
		}
		else {
			message = "Already Registered";
		}
		return message;
	}

}

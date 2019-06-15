package springframework.hipl.ems.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="break")
public class Break {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="break_id")
	private int id;
	@Column(name="break_start")
	private String breakStart;
	@Column(name = "break_end")
	private String breakEnd;
	@Column(name = "break_time")
	private String breakTime;
	@Column(name = "status")
	private int status;
	@ManyToMany
	@JoinTable(name="employee_breaks", 
				joinColumns = {@JoinColumn(name = "break_id")},
				inverseJoinColumns = {@JoinColumn(name = "employee_id")})
	private List<Employee> employees;
	@ManyToMany
	@JoinTable(name = "attendance_breaks",
				joinColumns = {@JoinColumn(name = "break_id")},
				inverseJoinColumns = {@JoinColumn(name = "attendance_id")})
	private List<Attendance> attendances;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBreakStart() {
		return breakStart;
	}
	public void setBreakStart(String breakStart) {
		this.breakStart = breakStart;
	}
	public String getBreakEnd() {
		return breakEnd;
	}
	public void setBreakEnd(String breakEnd) {
		this.breakEnd = breakEnd;
	}
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	public List<Attendance> getAttendances() {
		return attendances;
	}
	public void setAttendances(List<Attendance> attendances) {
		this.attendances = attendances;
	}
	
}

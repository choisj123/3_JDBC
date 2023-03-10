package edu.kh.jdbc1.model.vo;

public class Employee5_2 {
	
	private String empName;
	private String jobName;
	private int salary;
	private int annualIncome; // 연봉(연간 수입)
	
	private String hireDate;
	
	private String gender;


	public Employee5_2() {	}

	//Example4
	public Employee5_2(String empName, String jobName, int salary, int annualIncome) {
		super();
		this.empName = empName;
		this.jobName = jobName;
		this.salary = salary;
		this.annualIncome = annualIncome;
	}
	
	
	//Example5
	public Employee5_2(String empName, String hireDate, String gender) {
		super();
		this.empName = empName;
		this.hireDate = hireDate;
		this.gender = gender;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public int getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(int annualIncome) {
		this.annualIncome = annualIncome;
	}

	
	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
	@Override
	public String toString() {
		return empName + " / " + hireDate + " / " + gender;
	}
	
	

}

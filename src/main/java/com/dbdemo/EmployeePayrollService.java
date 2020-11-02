package com.dbdemo;

import java.time.LocalDate;
import java.util.*;

public class EmployeePayrollService {
	public enum IOService {
		DB_IO;
	};

	private List<EmployeePayrollData> employeePayrollList;
	private EmployeePayrollDBService employeePayrollDBService;

	public EmployeePayrollService() {
		employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this.employeePayrollList = employeePayrollList;
	}

	public List<EmployeePayrollData> readEmployeePayrollData(IOService dbIo) {
		if (dbIo.equals(IOService.DB_IO)) {
			this.employeePayrollList = employeePayrollDBService.readData();
		}
		return this.employeePayrollList;
	}

	public void updateEmployeeSalary(String name, double salary) {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSalary(salary);
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		for (EmployeePayrollData data : employeePayrollList) {
			if (data.getName().equals(name)) {
				return data;
			}
		}
		return null;
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name, double salary) {
		for (EmployeePayrollData data : employeePayrollList) {
			if (data.getName().equals(name)) {
				if (Double.compare(data.getSalary(), salary) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public void updateEmployeeSalaryUsingPrepareStatement(String name, double salary) {
		int result = employeePayrollDBService.updateEmployeeDataUsingPreparedStatement(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSalary(salary);
	}

	public List<EmployeePayrollData> readEmployeePayrollForDateRange(IOService dbIo, LocalDate startDate,
			LocalDate endDate) {
		if (dbIo.equals(IOService.DB_IO)) {
			return employeePayrollDBService.getEmployeeForGivenDateRange(startDate, endDate);
		}
		return null;
	}

	public Map<String, Double> readAverageSalaryByGender(IOService dbIo) {
		if (dbIo.equals(IOService.DB_IO)) {
			return employeePayrollDBService.getAverageSalaryByGender();
		}
		return null;
	}
	
	public void addEmployeeToPayroll(int id,String name, double salary, LocalDate startDate, String gender,String department) {
		EmployeePayrollData employeePayrollData = employeePayrollDBService.addEmployee(id,name, salary, startDate, gender,department);
		employeePayrollList.add(employeePayrollData);
	}
	
	public void addEmployeeToPayrollERDiagram(int id,String name, double salary, LocalDate startDate, String gender,String department,String phone,String address) {
		EmployeePayrollData employeePayrollData = employeePayrollDBService.addEmployeeToPayrollERDiagram(id,name, salary,
				startDate, gender,department,phone,address);
		employeePayrollList.add(employeePayrollData);
	}
	
	public void removeEmployee(String name) {
		employeePayrollDBService.removeEmployee(name);
	}
	public void addEmployeeToPayrollWithoutThreads(List<EmployeePayrollData> employeePayrollList) {
		employeePayrollList.forEach(employeePayrollData -> {
			System.out.println("Employee being added : " + employeePayrollData.name);
			this.addEmployeeToPayroll(employeePayrollData.id,employeePayrollData.name, employeePayrollData.salary, employeePayrollData.start, employeePayrollData.gender,employeePayrollData.department);
			System.out.println("Employee added : " + employeePayrollData.name);
		});
	}
	
	public void addEmployeeToPayrollWithThreads(List<EmployeePayrollData> employeePayrollList) {
		Map<Integer, Boolean> empAdditionStatus = new HashMap<Integer, Boolean>();
		employeePayrollList.forEach(employeePayrollData -> {
			Runnable task = () -> {
				empAdditionStatus.put(employeePayrollData.hashCode(), false);
				
				this.addEmployeeToPayroll(employeePayrollData.id,employeePayrollData.name, employeePayrollData.salary, employeePayrollData.start, employeePayrollData.gender,employeePayrollData.department);
				empAdditionStatus.put(employeePayrollData.hashCode(), true);
			};
			Thread thread = new Thread(task, employeePayrollData.name);
			thread.start();
		});
		while(empAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			}
			catch(InterruptedException e) {}
		}
	}
}

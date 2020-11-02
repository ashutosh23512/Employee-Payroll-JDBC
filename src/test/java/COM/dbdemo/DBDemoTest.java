package COM.dbdemo;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.dbdemo.DBDemo;
import com.dbdemo.EmployeePayrollData;
import com.dbdemo.EmployeePayrollService;
import com.dbdemo.EmployeePayrollService.IOService;

public class DBDemoTest {

	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		System.out.println(employeePayrollData);
		Assert.assertEquals(9, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Terisa", 3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa", 3000000.00);
		Assert.assertTrue(result);
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalaryUsingPrepareStatement("Terisa", 2000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa", 2000000.00);
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollForDateRange(IOService.DB_IO, startDate, endDate);
		Assert.assertEquals(9, employeePayrollData.size());
	}
	
	@Test
	public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(IOService.DB_IO);
		Assert.assertTrue(
				averageSalaryByGender.get("M").equals(2000000.00) && averageSalaryByGender.get("F").equals(3000000.00));
	}
	
	@Test
	public void givenNewEmployee_WhenAdded_ShouldSyncWityhDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.addEmployeeToPayroll(30,"Mark", 5000000.00, LocalDate.now(), "M","Sales");
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark", 5000000.00);
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenNewEmployee_WhenAddedToPayroll_ShouldSyncWityhDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.addEmployeeToPayrollERDiagram(34,"Glen", 5000000.00, LocalDate.now(), "M","Sales","9888888888","50,Model Town");
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Glen", 5000000.00);
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenEmployee_WhenRemovedFromPayroll_ShouldSyncWityhDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.removeEmployee("Glen");
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Glen", 5000000.00);
		Assert.assertTrue(result);
	}
	
	 @Test 
	    public void given3Employees_WhenAdded_ShouldMatchEmpCount() {
	    	EmployeePayrollData[] empPayrollData = {
	    			new EmployeePayrollData(50, "Jeff", 60000.0, LocalDate.now(),"M","sales"),
	    			new EmployeePayrollData(51, "Elon", 70000.0, LocalDate.now(),"M","maketing"),
	    			new EmployeePayrollData(52, "Tim", 50000.0, LocalDate.now(),"M","HR")
	    	};
	    	EmployeePayrollService empPayrollService = new EmployeePayrollService();
	    	empPayrollService.readEmployeePayrollData(IOService.DB_IO);
	    	Instant start = Instant.now();
	    	empPayrollService.addEmployeeToPayrollWithoutThreads(Arrays.asList(empPayrollData));
	    	Instant end = Instant.now();
	    	System.out.println("Duration without thread : " + Duration.between(start, end));
	    	List<EmployeePayrollData> employeePayrollData = empPayrollService.readEmployeePayrollData(IOService.DB_IO);
	    	Assert.assertEquals(25, employeePayrollData.size());
	    }
	 @Test 
	    public void given3Employees_WhenAdded_ShouldMatchEmpCountthreads() {
	    	EmployeePayrollData[] empPayrollData = {
	    			new EmployeePayrollData(53, "Jeff1", 60000.0, LocalDate.now(),"M","sales"),
	    			new EmployeePayrollData(54, "Elon1", 70000.0, LocalDate.now(),"M","maketing"),
	    			new EmployeePayrollData(55, "Tim1", 50000.0, LocalDate.now(),"M","HR")
	    	};
	    	EmployeePayrollService empPayrollService = new EmployeePayrollService();
	    	empPayrollService.readEmployeePayrollData(IOService.DB_IO);
	    	Instant start = Instant.now();
	    	empPayrollService.addEmployeeToPayrollWithThreads(Arrays.asList(empPayrollData));
	    	Instant end = Instant.now();
	    	System.out.println("Duration without thread : " + Duration.between(start, end));
	    	List<EmployeePayrollData> employeePayrollData = empPayrollService.readEmployeePayrollData(IOService.DB_IO);
	    	Assert.assertEquals(28, employeePayrollData.size());
	    }

}

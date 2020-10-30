package com.dbdemo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBDemo {
	static String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
	static String username = "root";
	static String password = "Ashutosh@23";
	static List<EmployeePayrollData> employeePayrollList = new ArrayList();

	public static void main(String[] args) {

		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");

		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find drivers", e);
		}
		listDrivers();

		try {
			System.out.println("Connecting to database: " + jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Database Connection Successful " + connection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<EmployeePayrollData> emplist = new ArrayList();
		

	}

	private static void listDrivers() {

		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			System.out.println("  " + driverClass.getClass().getName());
		}
	}

	


}

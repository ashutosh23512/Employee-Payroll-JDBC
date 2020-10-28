package COM.dbdemo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.dbdemo.DBDemo;
import com.dbdemo.EmployeePayrollData;

public class DBDemoTest {

	@Test
	public void CountEntries() {
		DBDemo dbdemo = new DBDemo();
		List<EmployeePayrollData> empdata = dbdemo.readData();
		Assert.assertEquals(4, empdata.size());
	}

}

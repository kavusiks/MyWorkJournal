package myworkjournal.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmployeeTest {
	
	@Test
	public void testConstructor() {
		// Check if name corresponds
		Employee employee = new Employee("Ola");
		assertEquals("Ola", employee.getName());
		
		// Check if it throws exception when number is in name
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
	        Employee employee1 = new Employee("1234Ola");
	    });
		Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
	        Employee employee3 = new Employee("");
	    });
		Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
	        Employee employee4 = new Employee("'-e");
	    });
		
	}
	
	@Test
	public void testSetName() {
		Employee employee = new Employee("Ola");
		employee.setName("OlaNordmann");
		assertEquals("OlaNordmann", employee.getName());
		employee.setName("Ola");
		assertEquals("Ola", employee.getName());
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
	        employee.setName("1234");
	    });
		Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
	        employee.setName("");
	    });
		Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
	        employee.setName("'-e");
	    });
		
	}
	
	@Test
	public void testAddWorkPeriod() {
		// WIP
	}
	
	@Test
	public void mergeTwoWorkPeriods() {
		// WIP
	}
	
	@Test
	public void testToString() {
		// WIP
	}
}

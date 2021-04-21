package myworkjournal.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeTest {
	private TestData testData = new TestData();
	private WorkPeriod nextMonthWorkPeriod = new WorkPeriod(testData.getValidNextMonth(), testData.getValidYear(), testData.getValidHourlyWage());
	private double totalHours;
	private double totalSalary;
	private int totalShifts;
	private int totalWorkPeriods;


	@BeforeEach
	public void setUp() {
		//The employee for this test has two workperiods with four works together
		nextMonthWorkPeriod.addWork(testData.getWorkNextMonth());
		testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth());
		testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth2());
		testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth3());

		testData.getEmployee().addWorkPeriod(testData.getThisMonthWorkPeriod());
		testData.getEmployee().addWorkPeriod(nextMonthWorkPeriod);

		totalHours = testData.getWorkNextMonth().getShiftDurationInHours() + testData.getWorkThisMonth().getShiftDurationInHours() + testData.getWorkThisMonth2().getShiftDurationInHours() + testData.getWorkThisMonth3().getShiftDurationInHours();
		totalSalary = totalHours * testData.getValidHourlyWage();
		totalShifts = testData.getEmployee().getWorkPeriods().stream().mapToInt(workPeriod -> (workPeriod.getPeriodWorkHistory().size())).sum();
		totalWorkPeriods = testData.getEmployee().getWorkPeriods().size();
	}



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

		//DETTE ER KUN DET SOM VAR I STATSTEST FRA FØR
		//assertNull(myStats, "The object should be null, since it isn't sat yet");
		//myStats = new MyStats(testData.getEmployee());
		assertNotNull(testData.getEmployee(), "The object wasn't sat correctly");
		String errorText = "The object doesn't contain correct work periods";
		assertTrue(testData.getEmployee().getWorkPeriods().contains(testData.getThisMonthWorkPeriod()), errorText);
		assertTrue(testData.getEmployee().getWorkPeriods().contains(nextMonthWorkPeriod), errorText);

	}

	@Test
	public void testGetters() {
		//DETTE ER KUN DET SOM VAR I STATSTEST FRA FØR
		//testing getTotalSalary()
		assertEquals(totalSalary, testData.getEmployee().getTotalSalary(), "The total salary wasn't as expected!");
		//testing getBestPaidWorkPeriod()  The best paid month is thisMonthWorkPeriod, since it has more shifts than nextMonthWorkPeriod. And all of the shift has the same hourlyWage and shiftDuration.
		assertEquals(testData.getThisMonthWorkPeriod(), testData.getEmployee().getBestPaidWorkPeriod(), "Wrong best paid workperiod returned.");
		//testing getAverageShiftAmount()
		assertEquals(totalShifts / totalWorkPeriods, testData.getEmployee().getAverageShiftAmount(), "Wrong amount of average shift returned.");
		//testing getAverageWorkHours()
		assertEquals(totalHours / totalWorkPeriods, testData.getEmployee().getAverageWorkHours(), "Wrong amount of average work hours returned.");
		//testing getAverageSalary()
		assertEquals(totalSalary / totalWorkPeriods, testData.getEmployee().getAverageSalary(), "Wrong amount of average salary returned.");
		//testing getAverageHourlyWage()  The average salary is the same as validHourlySalary, since all of the employee's workperiods has this as hourlyWage.
		assertEquals(testData.getValidHourlyWage(), testData.getEmployee().getAverageHourlyWage(), "Wrong amount of hourly wage returned.");
	}
	/*
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
		
	}*/
	
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

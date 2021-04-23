package myworkjournal.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {
	private TestData testData;
	private WorkPeriod nextMonthWorkPeriod;
	private double totalWorkHours;
	private double totalSalary;
	private int totalShifts;
	private int totalWorkPeriods;


	@BeforeEach
	public void setUp() {
		testData = new TestData();
		nextMonthWorkPeriod = new WorkPeriod(testData.getValidNextMonth(), testData.getValidYear(), testData.getValidHourlyWage());

		//The employee for this test has two workperiods with four works together
		nextMonthWorkPeriod.addWork(testData.getWorkNextMonth());
		testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth());
		testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth2());
		testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth3());

		testData.getEmployee().addWorkPeriod(testData.getThisMonthWorkPeriod());
		testData.getEmployee().addWorkPeriod(nextMonthWorkPeriod);

		totalWorkHours = testData.getWorkNextMonth().getShiftDurationInHours() + testData.getWorkThisMonth().getShiftDurationInHours() + testData.getWorkThisMonth2().getShiftDurationInHours() + testData.getWorkThisMonth3().getShiftDurationInHours();
		totalSalary = totalWorkHours * testData.getValidHourlyWage();
		totalShifts = testData.getEmployee().getWorkPeriods().stream().mapToInt(workPeriod -> (workPeriod.getPeriodWorkHistory().size())).sum();
		totalWorkPeriods = testData.getEmployee().getWorkPeriods().size();
	}

	@Test
	public void testConstructor() {
		// Check if name corresponds
		Employee employee = new Employee("Ola");
		assertEquals("Ola", employee.getName());
		assertTrue(employee.getWorkPeriods().isEmpty());

		// Check if it throws exception when number is in name
		assertThrows(IllegalArgumentException.class, () -> {
	        Employee employee1 = new Employee("1234Ola");
	    });
		assertThrows(IllegalArgumentException.class, () -> {
	        Employee employee3 = new Employee("");
	    });
		assertThrows(IllegalArgumentException.class, () -> {
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
	@DisplayName("Sjekk at getters fungerer. (getTotalSalary(), getBestPaidWorkPeriod(), getAverageShiftAmount(), getAverageWorkHours(), getAverageSalary(), getAverageHourlyWage()).")
	public void testGetters() {
		//DETTE ER KUN DET SOM VAR I STATSTEST FRA FØR
		//testing getTotalSalary()
		assertEquals(totalSalary, testData.getEmployee().getTotalSalary(), "The total salary wasn't as expected!");
		//testing getTotalWorkHours()
		assertEquals(totalWorkHours, testData.getEmployee().getTotalWorkHours(), "The total work hours wasn't as expected!");
		//testing getBestPaidWorkPeriod()  The best paid month is thisMonthWorkPeriod, since it has more shifts than nextMonthWorkPeriod. And all of the shift has the same hourlyWage and shiftDuration.
		assertEquals(testData.getThisMonthWorkPeriod(), testData.getEmployee().getBestPaidWorkPeriod(), "Wrong best paid workperiod returned.");
		//testing getAverageShiftAmount()
		assertEquals(totalShifts / totalWorkPeriods, testData.getEmployee().getAverageShiftAmount(), "Wrong amount of average shift returned.");
		//testing getAverageWorkHours()
		assertEquals(totalWorkHours / totalWorkPeriods, testData.getEmployee().getAverageWorkHours(), "Wrong amount of average work hours returned.");
		//testing getAverageSalary()
		assertEquals(totalSalary / totalWorkPeriods, testData.getEmployee().getAverageSalary(), "Wrong amount of average salary returned.");
		//testing getAverageHourlyWage()  The average salary is the same as validHourlySalary, since all of the employee's workperiods has this as hourlyWage.
		assertEquals(testData.getValidHourlyWage(), testData.getEmployee().getAverageHourlyWage(), "Wrong amount of hourly wage returned.");
	}
		
	@Test
	@DisplayName("testAddWorkPeriod() og testMergeTwoWorkPeriods().")
	public void testAddWorkPeriod() {
		Employee employee = new Employee("Ola");
				
		assertThrows(IllegalArgumentException.class, () -> {
			employee.addWorkPeriod(null);
		});
		assertTrue(employee.getWorkPeriods().isEmpty());
						
		employee.addWorkPeriod(nextMonthWorkPeriod);
		assertTrue(employee.getWorkPeriods().contains(nextMonthWorkPeriod));
		
		WorkPeriod wptest = new WorkPeriod("juni", 2021, 100);
		employee.addWorkPeriod(wptest);
		assertTrue(employee.getWorkPeriods().contains(wptest) && employee.getWorkPeriods().contains(nextMonthWorkPeriod));
		
	}
	
	@Test
	public void testRemoveWorkPeriod() {
		Employee employee = new Employee("Ola");
		WorkPeriod wptest = new WorkPeriod("juni", 2021, 100);
		
		// testing if it throws exception if workperiod is not there.
		assertThrows(IllegalArgumentException.class, () -> {
	        employee.removeWorkPeriod(nextMonthWorkPeriod);
	    });
		
		// testing if it throws exception if workperiod is null
		assertThrows(IllegalArgumentException.class, () -> {
			employee.removeWorkPeriod(null);
		});
		
		// testing if it removes a workperiod
		employee.addWorkPeriod(nextMonthWorkPeriod);
		employee.addWorkPeriod(wptest);
		assertTrue(employee.getWorkPeriods().contains(nextMonthWorkPeriod));
		assertTrue(employee.getWorkPeriods().contains(nextMonthWorkPeriod));
		employee.removeWorkPeriod(wptest);
		assertTrue(employee.getWorkPeriods().contains(nextMonthWorkPeriod));
		assertFalse(employee.getWorkPeriods().contains(wptest));
		assertEquals("[" +nextMonthWorkPeriod.toString() + "]", employee.getWorkPeriods().toString());
		
	}
	
	@Test
	public void testToString() {
		Employee employee = new Employee("Ola");
		assertEquals("Employee{" + "name='" + "Ola" + '\'' + ", workPeriods=" + "[]" + '}', employee.toString());
		employee.addWorkPeriod(nextMonthWorkPeriod);
		assertEquals("Employee{" + "name='" + "Ola" + '\'' + ", workPeriods=" + "[mai-2021]" + '}', employee.toString());
	}
}

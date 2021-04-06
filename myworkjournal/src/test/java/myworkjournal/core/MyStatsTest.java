package myworkjournal.core;

import myworkjournal.core.MyStats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyStatsTest extends CoreTestData{

	private MyStats myStats;
	private WorkPeriod nextMonthWorkPeriod = new WorkPeriod(validNextMonth, validYear, validHourlyWage);
	private int totalHours;
	private int totatSalary;
	private int totalShifts;
	private int totalWorkPeriods;


	@BeforeEach
	public void setUp() {
		//The employee for this test has two workperiods with four works together
		nextMonthWorkPeriod.addWork(workNextMonth);
		thisMonthWorkPeriod.addWork(workThisMonth);
		thisMonthWorkPeriod.addWork(workThisMonth2);
		thisMonthWorkPeriod.addWork(workThisMonth3);

		employee.addWorkPeriod(thisMonthWorkPeriod);
		employee.addWorkPeriod(nextMonthWorkPeriod);

		totalHours = workNextMonth.getHours() + workThisMonth.getHours() + workThisMonth2.getHours() + workThisMonth3.getHours();
		totatSalary = totalHours * validHourlyWage;
		totalShifts = employee.getWorkPeriods().values().stream().mapToInt(workPeriod -> (workPeriod.getPeriodWorkHistory().size())).sum();
		totalWorkPeriods = employee.getWorkPeriods().size();
	}

	@Test
	public void testConstructor() {
		assertNull(myStats, "The object should be null, since it isn't sat yet");
		myStats = new MyStats(employee);
		assertNotNull(myStats, "The object wasn't sat correctly");
		String errorText = "The object doesn't contain correct work periods";
		assertTrue(employee.getWorkPeriods().containsValue(thisMonthWorkPeriod), errorText);
		assertTrue(employee.getWorkPeriods().containsValue(nextMonthWorkPeriod), errorText);
	}

	@Test
	public void testGetters() {
		myStats = new MyStats(employee);
		//testing getTotalSalary()
		assertEquals(totatSalary, myStats.getTotalSalary(), "The total salary wasn't as expected!");
		//testing getBestPaidWorkPeriod()  The best paid month is thisMonthWorkPeriod, since it has more shifts than nextMonthWorkPeriod. And all of the shift has the same hourlyWage and shiftDuration.
		assertEquals(thisMonthWorkPeriod, myStats.getBestPaidWorkPeriod(), "Wrong best paid workperiod returned.");
		//testing getAverageShiftAmount()
		assertEquals(totalShifts / totalWorkPeriods, myStats.getAverageShiftAmount(), "Wrong amount of average shift returned.");
		//testing getAverageWorkHours()
		assertEquals(totalHours / totalWorkPeriods, myStats.getAverageWorkHours(), "Wrong amount of average work hours returned.");
		//testing getAverageSalary()
		assertEquals(totatSalary / totalWorkPeriods, myStats.getAverageSalary(), "Wrong amount of average salary returned.");
		//testing getAverageHourlyWage()  The average salary is the same as validHourlySalary, since all of the employee's workperiods has this as hourlyWage.
		assertEquals(validHourlyWage, myStats.getAverageHourlyWage(), "Wrong amount of hourly wage returned.");
	}


}

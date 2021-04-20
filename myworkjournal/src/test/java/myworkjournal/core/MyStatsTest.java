package myworkjournal.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyStatsTest {

	private TestData testData = new TestData();
	private MyStats myStats;
	private WorkPeriod nextMonthWorkPeriod = new WorkPeriod(testData.getValidNextMonth(), testData.getValidYear(), testData.getValidHourlyWage());
	private int totalHours;
	private int totalSalary;
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

		totalHours = testData.getWorkNextMonth().getHours() + testData.getWorkThisMonth().getHours() + testData.getWorkThisMonth2().getHours() + testData.getWorkThisMonth3().getHours();
		totalSalary = totalHours * testData.getValidHourlyWage();
		totalShifts = testData.getEmployee().getWorkPeriods().stream().mapToInt(workPeriod -> (workPeriod.getPeriodWorkHistory().size())).sum();
		totalWorkPeriods = testData.getEmployee().getWorkPeriods().size();
	}

	@Test
	public void testConstructor() {
		assertNull(myStats, "The object should be null, since it isn't sat yet");
		myStats = new MyStats(testData.getEmployee());
		assertNotNull(myStats, "The object wasn't sat correctly");
		String errorText = "The object doesn't contain correct work periods";
		assertTrue(testData.getEmployee().getWorkPeriods().contains(testData.getThisMonthWorkPeriod()), errorText);
		assertTrue(testData.getEmployee().getWorkPeriods().contains(nextMonthWorkPeriod), errorText);
	}

	@Test
	public void testGetters() {
		myStats = new MyStats(testData.getEmployee());
		//testing getTotalSalary()
		assertEquals(totalSalary, myStats.getTotalSalary(), "The total salary wasn't as expected!");
		//testing getBestPaidWorkPeriod()  The best paid month is thisMonthWorkPeriod, since it has more shifts than nextMonthWorkPeriod. And all of the shift has the same hourlyWage and shiftDuration.
		assertEquals(testData.getThisMonthWorkPeriod(), myStats.getBestPaidWorkPeriod(), "Wrong best paid workperiod returned.");
		//testing getAverageShiftAmount()
		assertEquals(totalShifts / totalWorkPeriods, myStats.getAverageShiftAmount(), "Wrong amount of average shift returned.");
		//testing getAverageWorkHours()
		assertEquals(totalHours / totalWorkPeriods, myStats.getAverageWorkHours(), "Wrong amount of average work hours returned.");
		//testing getAverageSalary()
		assertEquals(totalSalary / totalWorkPeriods, myStats.getAverageSalary(), "Wrong amount of average salary returned.");
		//testing getAverageHourlyWage()  The average salary is the same as validHourlySalary, since all of the employee's workperiods has this as hourlyWage.
		assertEquals(testData.getValidHourlyWage(), myStats.getAverageHourlyWage(), "Wrong amount of hourly wage returned.");
	}


}


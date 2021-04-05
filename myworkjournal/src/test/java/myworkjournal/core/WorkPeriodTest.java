package myworkjournal.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class WorkPeriodTest {
	//private LocalDateTime validStartTimeThisMonth1 = LocalDateTime.now();
	private int shiftDurationHours = 5;
	//private LocalDateTime validEndTimeThisMonth1 = LocalDateTime.now().plusHours(shiftDurationHours);
	private Work workThisMonth = new Work(LocalDateTime.now(), LocalDateTime.now().plusHours(shiftDurationHours));
	Work workThisMonth2 = new Work(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(shiftDurationHours).minusDays(1));
	private LocalDateTime validStartTimeNextMonth = LocalDateTime.now().plusMonths(1);
	private LocalDateTime validEndTimeNextMonth = validStartTimeNextMonth.plusHours(shiftDurationHours);
	private Work workNextMonth = new Work(validStartTimeNextMonth, validEndTimeNextMonth);
	private WorkPeriod workPeriod;
	int validYear = LocalDateTime.now().getYear();
	int invalidYear1 = 1;
	int invalidYear2 = validYear-2;
	int validHourlySalary = 150;
	int invalidHourlySalary = -150;
	String validMonth = "April";
	String invalidMonth = "Invalid";

	@BeforeEach
	public void setUp() {
		workPeriod = new WorkPeriod(validMonth, validYear, validHourlySalary);
	}



	@Test
	public void testConstructor() {
		WorkPeriod testCreateWorkPeriod = null;

		String testFailedMessage = "A workperiod with invalid month should not be created";
		try {
			testCreateWorkPeriod = new WorkPeriod(invalidMonth, validYear, validHourlySalary);
			fail(testFailedMessage);
		} catch (IllegalArgumentException e) {
			assertNull(testCreateWorkPeriod, testFailedMessage);
		}

		testFailedMessage = "A workperiod with invalid year as " + invalidYear1 + "should not be created.";
		try {
			testCreateWorkPeriod = new WorkPeriod(validMonth, invalidYear1, validHourlySalary);
			fail(testFailedMessage);
		} catch (IllegalArgumentException e) {
			assertNull(testCreateWorkPeriod, testFailedMessage);
		}

		testFailedMessage = "A workperiod with invalid year as " + invalidYear2 + "should not be created.";
		try {
			testCreateWorkPeriod = new WorkPeriod(validMonth, invalidYear2, validHourlySalary);
			fail(testFailedMessage);
		} catch (IllegalArgumentException e) {
			assertNull(testCreateWorkPeriod, testFailedMessage);
		}

		testFailedMessage = "A workperiod with invalid hourlywage as " + invalidHourlySalary + "should not be created.";
		try {
			testCreateWorkPeriod = new WorkPeriod(validMonth, validYear, invalidHourlySalary);
			fail(testFailedMessage);
		} catch (IllegalArgumentException e) {
			assertNull(testCreateWorkPeriod, testFailedMessage);
		}

		testCreateWorkPeriod = new WorkPeriod(validMonth, validYear, validHourlySalary);
		assertNotNull(testCreateWorkPeriod, "A valid workperiod couldn't be created");



	}


	@Test
	public void testAddWork() {
		assertEquals(0, workPeriod.getPeriodWorkHistory().size());
		workPeriod.addWork(workThisMonth);
		assertTrue(workPeriod.getPeriodWorkHistory().contains(workThisMonth), "Work was not properly added to the WorkPeriod");

		String testFailedMessage = "A work that is not in this period has been added to this period.";
		try {
			workPeriod.addWork(workNextMonth);
			fail(testFailedMessage);
		} catch (IllegalArgumentException e) {
			assertFalse(workPeriod.getPeriodWorkHistory().contains(workNextMonth), testFailedMessage);
		}

		testFailedMessage = "A duplicate work has been added";
		try {
			workPeriod.addWork(workThisMonth);
			fail(testFailedMessage);
		} catch (IllegalArgumentException e) {
			assertEquals(1, workPeriod.getPeriodWorkHistory().size(), testFailedMessage);
		}

		//Testing that a shift at the end of month is added correctly
		LocalDateTime validStartTimeEndOfThisMonth = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth());
		LocalDateTime validEndTimeEndOfThisMonth = validStartTimeEndOfThisMonth.plusHours(shiftDurationHours);
		Work workAtTheEndOfThisMonth = new Work(validStartTimeEndOfThisMonth, validEndTimeEndOfThisMonth);
		workPeriod.addWork(workAtTheEndOfThisMonth);
		assertTrue(workPeriod.getPeriodWorkHistory().contains(workAtTheEndOfThisMonth));

		//Testing that a shift that start at the end of the month, and ends next month will not be added
		LocalDateTime validStartTimeEndOfThisMonthOverNight = validStartTimeEndOfThisMonth.toLocalDate().atTime(23, 0);
		LocalDateTime invalidEndTimeEndOfThisMonthOverNight = validStartTimeEndOfThisMonthOverNight.plusHours(shiftDurationHours);
		Work workOverNightAtTheLastDayOfMonth = new Work(validStartTimeEndOfThisMonthOverNight, invalidEndTimeEndOfThisMonthOverNight);

		testFailedMessage = "A shift ending next month should not be added to this month's workPeriod.";
		try {
			workPeriod.addWork(workOverNightAtTheLastDayOfMonth);
			fail(testFailedMessage);
		} catch (IllegalArgumentException e) {
			assertFalse(workPeriod.getPeriodWorkHistory().contains(workOverNightAtTheLastDayOfMonth), testFailedMessage);
		}

		//Testing checkIfSameWork() and checkAlreadyAdded() here since they are private method used by addWork()
		workPeriod.setPeriodWorkHistory(new ArrayList<>());
		workPeriod.addWork(workThisMonth);
		try {
			workPeriod.addWork(workThisMonth);
			fail("An already existing shift was added");
		} catch (IllegalArgumentException e) {
			assertEquals("This work data already exists", e.getMessage());
		}




	}
	
	@Test
	public void testSetAndGetPeriodWorkHistory() {
		//Testing setPeriodWorkHistory()
		assertEquals(0, workPeriod.getPeriodWorkHistory().size(), "Verifying that the workPeriod contains no works before test setting workperiodhistory failed");
		Collection<Work> periodWorhistoryToAdd = new ArrayList<>(Arrays.asList(workThisMonth, workThisMonth2));
		workPeriod.setPeriodWorkHistory(periodWorhistoryToAdd);
		assertEquals(2, workPeriod.getPeriodWorkHistory().size(), "The periodworkhisotry was not set correctly");
		assertTrue(workPeriod.getPeriodWorkHistory().contains(workThisMonth), "The workperiod doesn't contain the correct works that was in the periodworkhistory which was sat for the workperiod..");
		assertTrue(workPeriod.getPeriodWorkHistory().contains(workThisMonth2), "The workperiod doesn't contain the correct works that was in the periodworkhistory which was sat for the workperiod..");
		//Testing getPeriodWorkHistory()
		assertEquals(periodWorhistoryToAdd, workPeriod.getPeriodWorkHistory());
	}
	


	@Test
	public void testGetters() {
		Work workThisMonth2 =new Work(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(shiftDurationHours).minusDays(1));
		workPeriod.addWork(workThisMonth);
		workPeriod.addWork(workThisMonth2);
		int totalWorkHours = shiftDurationHours * 2;
		//Testing getHourlyWage()
		assertEquals(validHourlySalary, workPeriod.getHourlyWage(), "getHourlyWage didn't give the correct amount.");
		//Testing getTotalHours()
		assertEquals(totalWorkHours, workPeriod.getTotalHours(), "getTotalHours() didn't give the correct amount.");
		//Testing getMonthSalary()
		assertEquals(totalWorkHours*validHourlySalary, workPeriod.getMonthSalary(), "getMonthSalary() didn't give the correct amount.");
		//Testing getIdentifier()
		String correctIdentifier = validMonth+"-"+validYear;
		assertEquals(correctIdentifier, workPeriod.getIdentifier(), "getIdentifier() didn't give the correct workperiod-identifier string");
		//Testing getPeriodStartDate()
		assertEquals(LocalDate.now().withDayOfMonth(1), workPeriod.getPeriodStartDate(), "getPeriodStartDate() didn't give the correct startdate for this period");
		//Testing getPeriodEndDate()
		assertEquals(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()), workPeriod.getPeriodEndDate(), "getPeriodEndDate() didn't give the correct enddate for this period");
	}


}

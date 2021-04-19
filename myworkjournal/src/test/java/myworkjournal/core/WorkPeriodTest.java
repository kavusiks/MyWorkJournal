package myworkjournal.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorkPeriodTest extends CoreTestData {
  private int invalidYear1 = 1;
  private int invalidYear2 = validYear - 2;
  private int invalidHourlyWage = -151;
  private String invalidMonth = "Invalid";


  @BeforeEach public void setUp() {
    thisMonthWorkPeriod = new WorkPeriod(validThisMonth, validYear, validHourlyWage);
  }



  @Test public void testConstructor() {
    WorkPeriod testCreateWorkPeriod = null;

    String testFailedMessage = "A workperiod with invalid month should not be created";
    try {
      testCreateWorkPeriod = new WorkPeriod(invalidMonth, validYear, validHourlyWage);
      fail(testFailedMessage);
    } catch (IllegalArgumentException e) {
      assertNull(testCreateWorkPeriod, testFailedMessage);
    }

    testFailedMessage = "A workperiod with invalid year as " + invalidYear1 + "should not be created.";
    try {
      testCreateWorkPeriod = new WorkPeriod(validThisMonth, invalidYear1, validHourlyWage);
      fail(testFailedMessage);
    } catch (IllegalArgumentException e) {
      assertNull(testCreateWorkPeriod, testFailedMessage);
    }

    testFailedMessage = "A workperiod with invalid year as " + invalidYear2 + "should not be created.";
    try {
      testCreateWorkPeriod = new WorkPeriod(validThisMonth, invalidYear2, validHourlyWage);
      fail(testFailedMessage);
    } catch (IllegalArgumentException e) {
      assertNull(testCreateWorkPeriod, testFailedMessage);
    }

    testFailedMessage = "A workperiod with invalid hourlywage as " + invalidHourlyWage + "should not be created.";
    try {
      testCreateWorkPeriod = new WorkPeriod(validThisMonth, validYear, invalidHourlyWage);
      fail(testFailedMessage);
    } catch (IllegalArgumentException e) {
      assertNull(testCreateWorkPeriod, testFailedMessage);
    }

    testCreateWorkPeriod = new WorkPeriod(validThisMonth, validYear, validHourlyWage);
    assertNotNull(testCreateWorkPeriod, "A valid workperiod couldn't be created");



  }


  @Test public void testAddWork() {
    assertEquals(0, thisMonthWorkPeriod.getPeriodWorkHistory().size());
    thisMonthWorkPeriod.addWork(workThisMonth);
    assertTrue(thisMonthWorkPeriod.getPeriodWorkHistory().contains(workThisMonth),
        "Work was not properly added to the WorkPeriod");

    String testFailedMessage = "A work that is not in this period has been added to this period.";
    try {
      thisMonthWorkPeriod.addWork(workNextMonth);
      fail(testFailedMessage);
    } catch (IllegalArgumentException e) {
      assertFalse(thisMonthWorkPeriod.getPeriodWorkHistory().contains(workNextMonth), testFailedMessage);
    }

    testFailedMessage = "A duplicate work has been added";
    try {
      thisMonthWorkPeriod.addWork(workThisMonth);
      fail(testFailedMessage);
    } catch (IllegalArgumentException e) {
      assertEquals(1, thisMonthWorkPeriod.getPeriodWorkHistory().size(), testFailedMessage);
    }

    //Testing that a shift at the end of month is added correctly
    LocalDateTime validStartTimeEndOfThisMonth =
        LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth());
    LocalDateTime validEndTimeEndOfThisMonth = validStartTimeEndOfThisMonth.plusHours(shiftDurationHours);
    Work workAtTheEndOfThisMonth = new Work(validStartTimeEndOfThisMonth, validEndTimeEndOfThisMonth);
    thisMonthWorkPeriod.addWork(workAtTheEndOfThisMonth);
    assertTrue(thisMonthWorkPeriod.getPeriodWorkHistory().contains(workAtTheEndOfThisMonth));

    //Testing that a shift that start at the end of the month, and ends next month will not be added
    LocalDateTime validStartTimeEndOfThisMonthOverNight = validStartTimeEndOfThisMonth.toLocalDate().atTime(23, 0);
    LocalDateTime invalidEndTimeEndOfThisMonthOverNight =
        validStartTimeEndOfThisMonthOverNight.plusHours(shiftDurationHours);
    Work workOverNightAtTheLastDayOfMonth =
        new Work(validStartTimeEndOfThisMonthOverNight, invalidEndTimeEndOfThisMonthOverNight);

    testFailedMessage = "A shift ending next month should not be added to this month's workPeriod.";
    try {
      thisMonthWorkPeriod.addWork(workOverNightAtTheLastDayOfMonth);
      fail(testFailedMessage);
    } catch (IllegalArgumentException e) {
      assertFalse(thisMonthWorkPeriod.getPeriodWorkHistory().contains(workOverNightAtTheLastDayOfMonth),
          testFailedMessage);
    }

    //Testing checkIfSameWork() and checkAlreadyAdded() here since they are private method used by addWork()
    thisMonthWorkPeriod.setPeriodWorkHistory(new ArrayList<>());
    thisMonthWorkPeriod.addWork(workThisMonth);
    try {
      thisMonthWorkPeriod.addWork(workThisMonth);
      fail("An already existing shift was added");
    } catch (IllegalArgumentException e) {
      assertEquals("This work data already exists", e.getMessage());
    }

  }

  @Test public void testRemoveWork() {
    assertEquals(0, thisMonthWorkPeriod.getPeriodWorkHistory().size());
    thisMonthWorkPeriod.addWork(workThisMonth);
    assertTrue(thisMonthWorkPeriod.getPeriodWorkHistory().contains(workThisMonth),
        "Some error occurred during adding work in testRemoveWork()");
    thisMonthWorkPeriod.removeWork(workThisMonth);
    assertFalse(thisMonthWorkPeriod.getPeriodWorkHistory().contains(workThisMonth),
        "Test failed in valid usage of removeWork()");

    //testing removeWork with invalid argument. Expecting an IllegalArgumentException
    String testFailedMessage =
        "An error wasn't thrown when trying to remove a work that doesn't contain in the WorkPeriod";
    try {
      thisMonthWorkPeriod.removeWork(workNextMonth);
      fail(testFailedMessage);
    } catch (IllegalArgumentException e) {
      assertFalse(thisMonthWorkPeriod.getPeriodWorkHistory().contains(workNextMonth), testFailedMessage);
    }
  }

  @Test public void testSetAndGetPeriodWorkHistory() {
    //Testing setPeriodWorkHistory()
    assertEquals(0, thisMonthWorkPeriod.getPeriodWorkHistory().size(),
        "Verifying that the workPeriod contains no works before test setting workperiodhistory failed");
    List<Work> periodWorhistoryToAdd = new ArrayList<>(Arrays.asList(workThisMonth, workThisMonth2));
    thisMonthWorkPeriod.setPeriodWorkHistory(periodWorhistoryToAdd);
    assertEquals(2, thisMonthWorkPeriod.getPeriodWorkHistory().size(), "The periodworkhisotry was not set correctly");
    assertTrue(thisMonthWorkPeriod.getPeriodWorkHistory().contains(workThisMonth),
        "The workperiod doesn't contain the correct works that was in the periodworkhistory which was sat for the workperiod..");
    assertTrue(thisMonthWorkPeriod.getPeriodWorkHistory().contains(workThisMonth2),
        "The workperiod doesn't contain the correct works that was in the periodworkhistory which was sat for the workperiod..");
    //Testing getPeriodWorkHistory()
    assertEquals(periodWorhistoryToAdd, thisMonthWorkPeriod.getPeriodWorkHistory());
  }



  @Test public void testGetters() {
    Work workThisMonth2 =
        new Work(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(shiftDurationHours).minusDays(1));
    thisMonthWorkPeriod.addWork(workThisMonth);
    thisMonthWorkPeriod.addWork(workThisMonth2);
    int totalWorkHours = shiftDurationHours * 2;
    //Testing getHourlyWage()
    assertEquals(validHourlyWage, thisMonthWorkPeriod.getHourlyWage(), "getHourlyWage didn't give the correct amount.");
    //Testing getTotalHours()
    assertEquals(totalWorkHours, thisMonthWorkPeriod.getTotalHours(),
        "getTotalHours() didn't give the correct amount.");
    //Testing getMonthSalary()
    assertEquals(totalWorkHours * validHourlyWage, thisMonthWorkPeriod.getMonthSalary(),
        "getMonthSalary() didn't give the correct amount.");
    //Testing getIdentifier()
    String correctIdentifier = validThisMonth + "-" + validYear;
    assertEquals(correctIdentifier, thisMonthWorkPeriod.getIdentifier(),
        "getIdentifier() didn't give the correct workperiod-identifier string");
    //Testing getPeriodStartDate()
    assertEquals(LocalDate.now().withDayOfMonth(1), thisMonthWorkPeriod.getPeriodStartDate(),
        "getPeriodStartDate() didn't give the correct startdate for this period");
    //Testing getPeriodEndDate()
    assertEquals(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()),
        thisMonthWorkPeriod.getPeriodEndDate(), "getPeriodEndDate() didn't give the correct enddate for this period");
  }


}

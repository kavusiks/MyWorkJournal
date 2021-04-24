package myworkjournal.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorkPeriodTest {
  WorkPeriod testCreateWorkPeriod;
  private TestData testData;
  private int invalidYear1;
  private int invalidYear2;
  private int invalidYear3;
  private int invalidHourlyWage;
  private String invalidMonth;

  @BeforeEach public void setUp() {
    testData = new TestData();
    invalidYear1 = 10;
    invalidYear2 = testData.getValidYear() - 10;
    invalidYear3 = testData.getValidYear() + 5;
    invalidHourlyWage = -151;
    invalidMonth = "Invalid";
  }


  @Test public void testConstructor() {

    //Testing with valid inputs
    testCreateWorkPeriod =
        new WorkPeriod(testData.getValidThisMonth(), testData.getValidYear(), testData.getValidHourlyWage());
    assertNotNull(testCreateWorkPeriod, "A valid workPeriod couldn't be created");

    //Testing with invalid inputs
    assertThrows(IllegalArgumentException.class, () -> testCreateWorkPeriod =
            new WorkPeriod(invalidMonth, testData.getValidYear(), testData.getValidHourlyWage()),
        "Expected an IllegalArgumentException to be thrown when creating a workPeriod with invalid month");

    assertThrows(IllegalArgumentException.class, () -> testCreateWorkPeriod =
            new WorkPeriod(testData.getValidThisMonth(), invalidYear1, testData.getValidHourlyWage()),
        "Expected an IllegalArgumentException to be thrown when creating a workPeriod with invalid year that is just a random number");


    assertThrows(IllegalArgumentException.class, () -> testCreateWorkPeriod =
            new WorkPeriod(testData.getValidThisMonth(), invalidYear2, testData.getValidHourlyWage()),
        "Expected an IllegalArgumentException to be thrown when creating a workPeriod with invalid year that is more than 4 year back from the current year");

    assertThrows(IllegalArgumentException.class, () -> testCreateWorkPeriod =
            new WorkPeriod(testData.getValidThisMonth(), invalidYear3, testData.getValidHourlyWage()),
        "Expected an IllegalArgumentException to be thrown when creating a workPeriod with invalid year that is later than 1 year back from the current year");



    assertThrows(IllegalArgumentException.class, () -> testCreateWorkPeriod =
            new WorkPeriod(testData.getValidThisMonth(), testData.getValidYear(), invalidHourlyWage),
        "Expected an IllegalArgumentException to be thrown when creating a workPeriod with invalid hourlyWage");

    assertThrows(IllegalArgumentException.class,
        () -> testCreateWorkPeriod = new WorkPeriod(null, testData.getValidYear(), testData.getValidHourlyWage()),
        "Expected an IllegalArgumentException to be thrown when creating a workPeriod with null as month");

    assertThrows(IllegalArgumentException.class,
        () -> testCreateWorkPeriod = new WorkPeriod(testData.getValidThisMonth(), testData.getValidYear(), 0),
        "Expected an IllegalArgumentException to be thrown when creating a workPeriod with 0 hourlyWage");


  }

  @Test public void testAddWork() {
    //Testing that null object can't be added
    assertThrows(IllegalArgumentException.class, () -> testData.getThisMonthWorkPeriod().addWork(null));

    //Testing valid works:
    assertEquals(0, testData.getThisMonthWorkPeriod().getPeriodWorkHistory().size());
    testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth());
    assertTrue(testData.getThisMonthWorkPeriod().getPeriodWorkHistory().contains(testData.getWorkThisMonth()),
        "Work was not properly added to the WorkPeriod");

    //Testing that a work at the end of month is added correctly
    LocalDateTime validStartTimeEndOfThisMonth =
        LocalDateTime.now().withHour(0).withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth());
    LocalDateTime validEndTimeEndOfThisMonth = validStartTimeEndOfThisMonth.plusHours(testData.getShiftDurationHours());
    Work workAtTheEndOfThisMonth = new Work(validStartTimeEndOfThisMonth, validEndTimeEndOfThisMonth);
    testData.getThisMonthWorkPeriod().addWork(workAtTheEndOfThisMonth);
    assertTrue(testData.getThisMonthWorkPeriod().getPeriodWorkHistory().contains(workAtTheEndOfThisMonth));

    //Testing with invalid works:
    String errorMessage = "A work shall not be added when an Exceptions is thrown!";

    //A Work that starts at the end of the month, and ends next month shall not be added
    LocalDateTime validStartTimeEndOfThisMonthOverNight = validStartTimeEndOfThisMonth.toLocalDate().atTime(23, 0);
    LocalDateTime invalidEndTimeEndOfThisMonthOverNight =
        validStartTimeEndOfThisMonthOverNight.plusHours(testData.getShiftDurationHours());
    Work workOverNightAtTheLastDayOfMonth =
        new Work(validStartTimeEndOfThisMonthOverNight, invalidEndTimeEndOfThisMonthOverNight);

    assertThrows(IllegalArgumentException.class,
        () -> testData.getThisMonthWorkPeriod().addWork(testData.getWorkNextMonth()),
        "Expects an IllegalArgumentException when adding a work that is not in the period we want to add to.");
    assertFalse(testData.getThisMonthWorkPeriod().getPeriodWorkHistory().contains(testData.getWorkNextMonth()),
        errorMessage);

    int sizeBeforeAdding = testData.getThisMonthWorkPeriod().getPeriodWorkHistory().size();
    assertThrows(IllegalArgumentException.class,
        () -> testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth()),
        "Expects an IllegalArgumentException when adding an already existing work.");
    assertEquals(sizeBeforeAdding, testData.getThisMonthWorkPeriod().getPeriodWorkHistory().size(), errorMessage);

    assertThrows(IllegalArgumentException.class,
        () -> testData.getThisMonthWorkPeriod().addWork(workOverNightAtTheLastDayOfMonth),
        "Expects an IllegalArgumentException when adding a work ending next month");
    assertFalse(testData.getThisMonthWorkPeriod().getPeriodWorkHistory().contains(workOverNightAtTheLastDayOfMonth),
        errorMessage);

    //Testing checkWorkAlreadyAdded() here since it is a private method used by addWork()
    testData.getThisMonthWorkPeriod().setPeriodWorkHistory(new ArrayList<>());
    testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth());
    assertThrows(IllegalArgumentException.class,
        () -> testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth()),
        "Expects an IllegalArgumentException when adding the same work again");


    errorMessage =
        "Expects an IllegalArgumentExceptions to be thrown when adding an overlapping work. A workperiod can only have one shift at a time!";

    //Overlapping startTime
    assertThrows(IllegalArgumentException.class, () -> {
      //Testing checkIfAnyOverlappingWorksExists here since it is a private method used by addWork()
      testData.getThisMonthWorkPeriod().addWork(new Work(testData.getWorkThisMonth().getStartTime().plusHours(1),
          testData.getWorkThisMonth().getEndTime().plusHours(1)));
    }, errorMessage);

    //Overlapping endTime
    assertThrows(IllegalArgumentException.class, () -> testData.getThisMonthWorkPeriod().addWork(
        new Work(testData.getWorkThisMonth().getStartTime().minusHours(1),
            testData.getWorkThisMonth().getEndTime().minusHours(1))), errorMessage);

    //Overlapping startTime and endTime, a work within another existing work
    assertThrows(IllegalArgumentException.class, () -> testData.getThisMonthWorkPeriod().addWork(
        new Work(testData.getWorkThisMonth().getStartTime().plusHours(1),
            testData.getWorkThisMonth().getEndTime().minusHours(1))), errorMessage);


    //Adding a work that covers an existing work.
    assertThrows(IllegalArgumentException.class, () -> testData.getThisMonthWorkPeriod().addWork(
        new Work(testData.getWorkThisMonth().getStartTime().minusHours(1),
            testData.getWorkThisMonth().getEndTime().plusHours(1))), errorMessage);

  }

  @Test public void testRemoveWork() {
    assertEquals(0, testData.getThisMonthWorkPeriod().getPeriodWorkHistory().size());
    testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth());
    assertTrue(testData.getThisMonthWorkPeriod().getPeriodWorkHistory().contains(testData.getWorkThisMonth()),
        "Some error occurred during adding work in testRemoveWork()");
    testData.getThisMonthWorkPeriod().removeWork(testData.getWorkThisMonth());
    assertFalse(testData.getThisMonthWorkPeriod().getPeriodWorkHistory().contains(testData.getWorkThisMonth()),
        "Test failed in valid usage of removeWork()");

    //testing removeWork with invalid argument. Expecting an IllegalArgumentException
    String testFailedMessage =
        "An error wasn't thrown when trying to remove a work that doesn't exist in the WorkPeriod";
    assertThrows(IllegalArgumentException.class, () -> {
          //Verifying that the Work we'll try to remove doesn't exist in the WorkPeriod.
          assertFalse(testData.getThisMonthWorkPeriod().getPeriodWorkHistory().contains(testData.getWorkNextMonth()),
              "The WorkPeriod should not contain the given Work.");
          testData.getThisMonthWorkPeriod().removeWork(testData.getWorkNextMonth());
        },
        "Expecting an IllegalArgumentException to be thrown when trying to remove a work that doesn't exist in the WorkPeriod");

  }

  @Test public void testSetAndGetPeriodWorkHistory() {
    //Testing setPeriodWorkHistory()
    assertEquals(0, testData.getThisMonthWorkPeriod().getPeriodWorkHistory().size(),
        "Verifying that the workPeriod contains no works before test setting workperiodhistory failed");
    List<Work> periodWorhistoryToAdd =
        new ArrayList<>(Arrays.asList(testData.getWorkThisMonth(), testData.getWorkThisMonth2()));
    testData.getThisMonthWorkPeriod().setPeriodWorkHistory(periodWorhistoryToAdd);
    assertEquals(2, testData.getThisMonthWorkPeriod().getPeriodWorkHistory().size(),
        "The periodworkhisotry was not set correctly");
    assertTrue(testData.getThisMonthWorkPeriod().getPeriodWorkHistory().contains(testData.getWorkThisMonth()),
        "The workperiod doesn't contain the correct works that was in the periodworkhistory which was sat for the workperiod..");
    assertTrue(testData.getThisMonthWorkPeriod().getPeriodWorkHistory().contains(testData.getWorkThisMonth2()),
        "The workperiod doesn't contain the correct works that was in the periodworkhistory which was sat for the workperiod..");
    //Testing getPeriodWorkHistory()
    assertEquals(periodWorhistoryToAdd, testData.getThisMonthWorkPeriod().getPeriodWorkHistory());
  }

  @Test
  @DisplayName("Sjekk at getters fungerer. (getHourlyWage(), getTotalHours(), getMonthSalary(), getIdentifier(), getPeriodStartDate(), getPeriodEndDate()).")
  public void testGetters() {
    Work workThisMonth2 = new Work(LocalDateTime.now().minusDays(1),
        LocalDateTime.now().plusHours(testData.getShiftDurationHours()).minusDays(1));
    testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth());
    testData.getThisMonthWorkPeriod().addWork(workThisMonth2);
    int totalWorkHours = testData.getShiftDurationHours() * 2;
    //Testing getHourlyWage()
    assertEquals(testData.getValidHourlyWage(), testData.getThisMonthWorkPeriod().getHourlyWage(),
        "getHourlyWage() didn't give the correct amount.");
    //Testing getTotalHours()
    assertEquals(totalWorkHours, testData.getThisMonthWorkPeriod().getTotalHours(),
        "getTotalHours() didn't give the correct amount.");
    //Testing getMonthSalary()
    assertEquals(totalWorkHours * testData.getValidHourlyWage(), testData.getThisMonthWorkPeriod().getMonthSalary(),
        "getMonthSalary() didn't give the correct amount.");
    //Testing getIdentifier()
    String correctIdentifier = testData.getValidThisMonth() + "-" + testData.getValidYear();
    assertEquals(correctIdentifier, testData.getThisMonthWorkPeriod().getIdentifier(),
        "getIdentifier() didn't give the correct workperiod-identifier string");
    //Testing getPeriodStartDate()
    assertEquals(LocalDate.now().withDayOfMonth(1), testData.getThisMonthWorkPeriod().getPeriodStartDate(),
        "getPeriodStartDate() didn't give the correct startdate for this period");
    //Testing getPeriodEndDate()
    assertEquals(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()),
        testData.getThisMonthWorkPeriod().getPeriodEndDate(),
        "getPeriodEndDate() didn't give the correct enddate for this period");
  }

}

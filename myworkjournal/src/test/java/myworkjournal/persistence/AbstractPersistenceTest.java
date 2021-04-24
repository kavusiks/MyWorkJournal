package myworkjournal.persistence;

import myworkjournal.core.TestData;
import myworkjournal.core.Employee;
import myworkjournal.core.Work;
import myworkjournal.core.WorkPeriod;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

public abstract class AbstractPersistenceTest {


  protected String filepath;
  protected String invalidPath = "invalid";
  protected TestData testData = new TestData();

  public AbstractPersistenceTest() {

  }

  protected void assertFileIsEmpty(String filepath) {
    File file = new File(filepath);
    assertEquals(0, file.length(), "The file was not empty as expected");
  }

  protected void assertFileIsNotEmpty(String filepath){
    File file = new File(filepath);
    assertNotEquals(0, file.length(), "The file was empty as expected");

  }

  protected void assertSameWork(Work expected, Work actual, String errorText) {
    assertEquals(expected.getStartTime(),actual.getStartTime(), errorText);
    assertEquals(expected.getEndTime(),actual.getEndTime(), errorText);
  }

  protected void assertSameWorkPeriod(WorkPeriod expected, WorkPeriod actual, String errorText) {
    assertEquals(expected.getIdentifier(),actual.getIdentifier(), errorText);
    assertEquals(expected.getPeriodStartDate(),actual.getPeriodStartDate(), errorText);
    assertEquals(expected.getPeriodEndDate(),actual.getPeriodEndDate(), errorText);
    assertEquals(expected.getTotalHours(),actual.getTotalHours(), errorText);
    assertEquals(expected.getMonthSalary(),actual.getMonthSalary(), errorText);
    assertEquals(expected.getHourlyWage(),actual.getHourlyWage(), errorText);
    assertEquals(expected.getPeriodWorkHistory().size(), actual.getPeriodWorkHistory().size());
    if(actual.getPeriodWorkHistory().size() > 0) {
      for (Work workActual : actual) {
        for (Work workExpected: expected) {
          if (workExpected.getStartTime().equals(workActual.getStartTime()) && workExpected.getEndTime().equals(workActual.getEndTime())) {
            assertSameWork(workExpected, workActual, errorText + "Because the workPeriods didn't contain the same works.");
          }
        }
      }
    }
  }

  protected void assertSameEmployee(Employee expected, Employee actual, String errorText) {
    assertEquals(expected.getName(),actual.getName(), errorText);
    assertEquals(expected.getWorkPeriods().size(),actual.getWorkPeriods().size(), errorText);
    if(actual.getWorkPeriods().size() > 0) {
      for (WorkPeriod workPeriodActual : actual) {
        for (WorkPeriod workPeriodExpected: expected) {
          if (workPeriodExpected.getIdentifier().equals(workPeriodActual.getIdentifier())) {
            assertSameWorkPeriod(workPeriodExpected, workPeriodActual, errorText + "Because the Employees didn't contain the same workPeriods.");
          }
        }
      }
    }
  }

}

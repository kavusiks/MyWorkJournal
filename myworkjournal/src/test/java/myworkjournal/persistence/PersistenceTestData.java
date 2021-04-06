package myworkjournal.persistence;

import myworkjournal.core.CoreTestData;
import myworkjournal.core.Work;
import myworkjournal.core.WorkPeriod;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

public class PersistenceTestData extends CoreTestData {


  String filepath;
  String invalidPath = "invalid";

  public PersistenceTestData() {

  }

  public void assertFileIsEmpty(String filepath) {
    File file = new File(filepath);
    assertEquals(0, file.length(), "The file was not empty as expected");
  }

  public void assertFileIsNotEmpty(String filepath){
    File file = new File(filepath);
    assertNotEquals(0, file.length(), "The file was empty as expected");

  }

  public void assertSameWork(Work expected, Work actual, String errorText) {
    assertEquals(expected.getStartTime(),actual.getStartTime(), errorText);
    assertEquals(expected.getEndTime(),actual.getEndTime(), errorText);
  }

  public void assertSameWorkPeriod(WorkPeriod expected, WorkPeriod actual, String errorText) {
    assertEquals(expected.getIdentifier(),actual.getIdentifier(), errorText);
    assertEquals(expected.getPeriodStartDate(),actual.getPeriodStartDate(), errorText);
    assertEquals(expected.getPeriodEndDate(),actual.getPeriodEndDate(), errorText);
    assertEquals(expected.getTotalHours(),actual.getTotalHours(), errorText);
    assertEquals(expected.getMonthSalary(),actual.getMonthSalary(), errorText);
    assertEquals(expected.getHourlyWage(),actual.getHourlyWage(), errorText);
    assertEquals(expected.getPeriodWorkHistory().size(), actual.getPeriodWorkHistory().size());
    //TODO: også sjekk inneholdet i lista
  }

}

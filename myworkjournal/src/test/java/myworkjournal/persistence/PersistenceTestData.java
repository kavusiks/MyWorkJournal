package myworkjournal.persistence;

import myworkjournal.core.CoreTestData;
import myworkjournal.core.Work;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

public class PersistenceTestData extends CoreTestData {


  String filepath;
  String invalidPath = "invalid";

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

}

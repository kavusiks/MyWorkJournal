package myworkjournal.persistence;

import myworkjournal.core.WorkPeriod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WorkPeriodPersistenceTest extends AbstractPersistenceTest implements PersistenceTestInterface {

  private WorkPeriodPersistence workPeriodPersistence;
  private WorkPeriod readWorkPeriod;



  /**
   * Method used to delete the savefile created while testing persistence.
   */
  @AfterEach public void cleanUp() throws IOException {
    File fileToErase = new File(filepath);
    if (fileToErase.length() > 0 && !fileToErase.delete())
      throw new IOException("Failed to delete not empty saveFile in cleanUp()");
  }

  @BeforeEach @Override public void setUp() {
    filepath = "src/test/resources/myworkjournal/persistence/workPeriod.txt";
  }


  @Test @Override public void testConstructor() {
    workPeriodPersistence = null;
    workPeriodPersistence = new WorkPeriodPersistence(filepath);
    assertNotNull(workPeriodPersistence, "The workPeriodPersistence was not created properly.");

  }

  /**
   * Common method used to test reading and writing from/to the savefile in the persistence class.
   * serialize() and deserialize() are tested within the readFile() and writeFile()
   */
  @Test @Override public void testWriteAndReadFile() {

    //Testing readFile() from invalid filepath
    workPeriodPersistence = new WorkPeriodPersistence(invalidPath);
    assertThrows(FileNotFoundException.class, () -> readWorkPeriod = workPeriodPersistence.readFile(),
        "FileNotFoundExceptions was not thrown");
    assertNull(readWorkPeriod, "No work should be read, when there are no valid files.");

    //Testing writeFile() with valid filepath
    //Testing with no work in periodWorkHistory()
    workPeriodPersistence = new WorkPeriodPersistence(filepath);
    assertFileIsEmpty(filepath);
    try {
      workPeriodPersistence.writeFile(testData.getThisMonthWorkPeriod());
      assertFileIsNotEmpty(filepath);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("WorkPeriodPersistence was not able to write to the correct path");
    }

    //Testing readFile() by reading the data saved from the sub-test above
    workPeriodPersistence = new WorkPeriodPersistence(filepath);
    try {
      readWorkPeriod = workPeriodPersistence.readFile();
      assertSameWorkPeriod(testData.getThisMonthWorkPeriod(), readWorkPeriod,
          "The read workPeriod was not the written workPeriod.");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("WorkPeriodPersistence was not able to read from correct path");
    }

  }

  @Test public void testWriteAndReadFileWithSingleWork() {
    //Testing with one work in periodWorkHistory()
    testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth());
    workPeriodPersistence = new WorkPeriodPersistence(filepath);
    assertFileIsEmpty(filepath);
    try {
      workPeriodPersistence.writeFile(testData.getThisMonthWorkPeriod());
      assertFileIsNotEmpty(filepath);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("WorkPeriodPersistence was not able to write to the correct path");
    }

    //Testing readFile() by reading the data saved from the sub-test above
    workPeriodPersistence = new WorkPeriodPersistence(filepath);
    try {
      readWorkPeriod = workPeriodPersistence.readFile();
      assertSameWorkPeriod(testData.getThisMonthWorkPeriod(), readWorkPeriod,
          "The read workPeriod was not the written workPeriod.");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("WorkPeriodPersistence was not able to read from correct path");
    }
  }

  @Test public void testWriteAndReadFileWithMultipleWork() {
    //Testing with two work in periodWorkHistory()
    testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth());
    testData.getThisMonthWorkPeriod().addWork(testData.getWorkThisMonth2());
    workPeriodPersistence = new WorkPeriodPersistence(filepath);
    assertFileIsEmpty(filepath);
    try {
      workPeriodPersistence.writeFile(testData.getThisMonthWorkPeriod());
      assertFileIsNotEmpty(filepath);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("WorkPeriodPersistence was not able to write to the correct path");
    }

    //Testing readFile() by reading the data saved from the sub-test above
    workPeriodPersistence = new WorkPeriodPersistence(filepath);
    try {
      readWorkPeriod = workPeriodPersistence.readFile();
      assertSameWorkPeriod(testData.getThisMonthWorkPeriod(), readWorkPeriod,
          "The read workPeriod was not the written workPeriod.");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("WorkPeriodPersistence was not able to read from correct path");
    }
  }

}

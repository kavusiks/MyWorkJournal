package myworkjournal.persistence;

import myworkjournal.core.Work;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WorkPersistenceTest extends AbstractPersistenceTest implements PersistenceTestInterface {

  private WorkPersistence workPersistence;
  private Work readWork;

  /**
   * Method used to delete the savefile created while testing persistence.
   */
  @AfterEach public void cleanUp() throws IOException {
    File fileToErase = new File(filepath);
    if (fileToErase.length() > 0 && !fileToErase.delete())
      throw new IOException("Failed to delete not empty saveFile in cleanUp()");
  }

  @BeforeEach @Override public void setUp() {
    filepath = "src/test/resources/myworkjournal/persistence/work.txt";
  }

  @Test @Override public void testConstructor() {
    workPersistence = null;
    workPersistence = new WorkPersistence(filepath);
    assertNotNull(workPersistence, "The workPersistence was not created properly.");

  }

  /**
   * Common method used to test reading and writing from/to the savefile in the persistence class.
   * serialize() and deserialize() are tested within the readFile() and writeFile()
   */
  @Test @Override public void testWriteAndReadFile() {

    //Testing writeFile() with valid filepath
    workPersistence = new WorkPersistence(filepath);
    assertFileIsEmpty(filepath);
    try {
      workPersistence.writeFile(testData.getWorkThisMonth());
      assertFileIsNotEmpty(filepath);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("WorkPersistence was not able to write to the correct path");
    }

    //Testing readFile() by reading the data saved from the sub-test above
    workPersistence = new WorkPersistence(filepath);
    try {
      readWork = workPersistence.readFile();
      assertSameWork(testData.getWorkThisMonth(), readWork, "The read work was not the written work.");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("WorkPersistence was not able to read from correct path");
    }

    //Testing readFile() from invalid filepath
    readWork = null;
    workPersistence = new WorkPersistence(invalidPath);
    assertThrows(FileNotFoundException.class, () -> readWork = workPersistence.readFile(),
        "FileNotFoundExceptions was not thrown");
    assertNull(readWork, "No workPeriod should be read, when there are no valid files.");

  }

}

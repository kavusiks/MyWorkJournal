package myworkjournal.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class WorkPersistenceTest extends PersistenceTest implements PersistenceTestInterface {

	WorkPersistence workPersistence;

	/**
	 * Method used to delete the savefile created while testing persistence.
	 */
	@AfterEach
	public void cleanUp() {
		File fileToErase = new File(filepath);
		fileToErase.delete();
	}

	@BeforeEach
	@Override public void setUp() {
		filepath = "src/test/resources/myworkjournal/persistence/work.txt";
	}

	@Test
	@Override public void testConstructor() {
		workPersistence = null;
		workPersistence = new WorkPersistence(filepath);
		assertNotNull(workPersistence, "The workpersistence without work was not created properly.");
		workPersistence = new WorkPersistence(filepath, testData.getWorkThisMonth());
		assertNotNull(workPersistence, "The workpersistence with work was not created properly.");
		assertEquals(testData.getWorkThisMonth(), workPersistence.getWork(), "The workpersistence with work was created, but doesn't contain the correct work.");

	}

	/**
	 * Common method used to test reading and writing from/to the savefile in the persistence class.
	 * serialize() and deserialize() are tested within the readFile() and writeFile()
	 */
	@Test
	@Override public void testWriteAndReadFile() {
		//Testing writeFile() with invalid filepath
		/*
		workPersistence = new WorkPersistence(invalidPath, workThisMonth);
		try {
			workPersistence.writeFile();
			fail("Work should not be saved when the savepath is invalid. FileNotFoundExceptions was not thrown.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/


		//Testing writeFile() with valid filepath
		workPersistence = new WorkPersistence(filepath, testData.getWorkThisMonth());
		assertFileIsEmpty(filepath);
		try {
			workPersistence.writeFile();
			assertFileIsNotEmpty(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("WorkPersistence was not able to write to the correct path");
		}

		//Testing readFile() by reading the data saved from the sub-test above
		workPersistence = new WorkPersistence(filepath);
		try {
			workPersistence.readFile();
			assertSameWork(testData.getWorkThisMonth(), workPersistence.getWork(), "The read work was not the written work.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("WorkPersistence was not able to read from correct path");
		}

		//Testing readFile() froom invalid filepath
		workPersistence = new WorkPersistence(invalidPath);
		try {
			workPersistence.readFile();
			fail("FileNotFoundExceptions was not thrown");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			assertNull(workPersistence.getWork(), "No workPeriod should be read, when there are no valid files.");
		}



	}

}

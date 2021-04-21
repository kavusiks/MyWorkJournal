package myworkjournal.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class WorkPeriodPersistenceTest extends PersistenceTest implements PersistenceTestInterface {

	private WorkPeriodPersistence workPeriodPersistence;



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
		filepath = "src/test/resources/myworkjournal/persistence/workPeriod.txt";
	}


	@Test
	@Override public void testConstructor() {
		workPeriodPersistence = null;
		workPeriodPersistence = new WorkPeriodPersistence(filepath);
		assertNotNull(workPeriodPersistence, "The workpersistence without work was not created properly.");
		//workPeriodPersistence = new WorkPeriodPersistence(filepath, testData.getThisMonthWorkPeriod());
		//assertNotNull(workPeriodPersistence, "The workpersistence with work was not created properly.");
		//assertEquals(testData.getThisMonthWorkPeriod(), workPeriodPersistence.getWorkPeriod(), "The workPeriodpersistence with workPeriod was created, but doesn't contain the correct workPeriod.");

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


		//Testing readFile() froom invalid filepath
		workPeriodPersistence = new WorkPeriodPersistence(invalidPath);
		try {
//			workPeriodPersistence.readFile();
			assertNull(workPeriodPersistence.readFile(), "No work should be read, when there are no valid files.");
			fail("FileNotFoundExceptions was not thrown");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

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
			//workPeriodPersistence.readFile();
			assertSameWorkPeriod(testData.getThisMonthWorkPeriod(), workPeriodPersistence.readFile(), "The read workPeriod was not the written workPeriod.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("WorkPeriodPersistence was not able to read from correct path");
		}

	}

	@Test
	public void testWriteAndReadFileWithSingleWork() {
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
			workPeriodPersistence.readFile();
			assertSameWorkPeriod(testData.getThisMonthWorkPeriod(), testData.getThisMonthWorkPeriod(), "The read workPeriod was not the written workPeriod.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("WorkPeriodPersistence was not able to read from correct path");
		}
	}

	@Test
	public void testWriteAndReadFileWithMultipleWork() {
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
			workPeriodPersistence.readFile();
			assertSameWorkPeriod(testData.getThisMonthWorkPeriod(), workPeriodPersistence.readFile(), "The read workPeriod was not the written workPeriod.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("WorkPeriodPersistence was not able to read from correct path");
		}
	}

}

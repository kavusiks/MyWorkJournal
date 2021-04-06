package myworkjournal.persistence;

import myworkjournal.persistence.EmployeePersistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeePersistenceTest extends PersistenceTestData implements PersistenceTestInterface{

	private EmployeePersistence employeePersistence;

//TODO: finn ut av evig loop ved tom employee

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
		filepath = "src/test/resources/myworkjournal/persistence/employee.txt";
	}

	@Test
	@Override public void testConstructor() {
		System.out.println("1");
		employeePersistence = null;
		System.out.println("1");
		employeePersistence = new EmployeePersistence(filepath);
		assertNotNull(employeePersistence, "The employeePersistence without work was not created properly.");
		employeePersistence = new EmployeePersistence(filepath, employee);
		assertNotNull(employeePersistence, "The employeePersistence with work was not created properly.");
		assertEquals(employee, employeePersistence.getEmployee(), "The employeePersistence with employee was created, but it doesn't contain the correct employee.");


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

		//TODO: Også vurder å teste med innhold


		//Testing writeFile() with valid filepath
		employeePersistence = new EmployeePersistence(filepath, employee);
		assertFileIsEmpty(filepath);
		try {
			employeePersistence.writeFile();
			assertFileIsNotEmpty(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("employeePersistence was not able to write to the correct path");
		}

		//Testing readFile() by reading the data saved from the sub-test above
		employeePersistence = new EmployeePersistence(filepath);
		try {
			employeePersistence.readFile();
			assertSameEmployee(employee, employeePersistence.getEmployee(), "The read employee was not the written employee.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("employeePersistence was not able to read from correct path");
		}

		//Testing readFile() froom invalid filepath
		employeePersistence = new EmployeePersistence(invalidPath);
		try {
			employeePersistence.readFile();
			fail("FileNotFoundExceptions was not thrown");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			assertNull(employeePersistence.getEmployee(), "No employee should be read, when there are no valid files.");
		}



	}

}

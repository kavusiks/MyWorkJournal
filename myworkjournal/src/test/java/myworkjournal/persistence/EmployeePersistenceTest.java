package myworkjournal.persistence;

import myworkjournal.core.Employee;
import myworkjournal.core.WorkPeriod;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeePersistenceTest extends AbstractPersistenceTest implements PersistenceTestInterface{

	private EmployeePersistence employeePersistence;
	private Employee readEmployee;

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
		employeePersistence = new EmployeePersistence(filepath);
		assertNotNull(employeePersistence, "The employeePersistence without work was not created properly.");
		employeePersistence = new EmployeePersistence(filepath);
		assertNotNull(employeePersistence, "The employeePersistence with work was not created properly.");
		assertEquals(testData.getEmployee(), testData.getEmployee(), "The employeePersistence with employee was created, but it doesn't contain the correct employee.");


	}

	/**
	 * Common method used to test reading and writing from/to the savefile in the persistence class.
	 * serialize() and deserialize() are tested within the readFile() and writeFile()
	 */
	@Test
	@Override public void testWriteAndReadFile() {

		//Testing readFile() from invalid filepath
		employeePersistence = new EmployeePersistence(invalidPath);
		assertThrows(FileNotFoundException.class, () -> {
			readEmployee = employeePersistence.readFile();

		},"FileNotFoundExceptions was not thrown");
		assertNull(readEmployee, "No employee should be read, when there are no valid files.");



		//Testing writeFile() with valid filepath
		//Testing with no workPeriods in workPeriods-hashmap
		employeePersistence = new EmployeePersistence(filepath);
		assertFileIsEmpty(filepath);
		try {
			employeePersistence.writeFile(testData.getEmployee());
			assertFileIsNotEmpty(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("employeePersistence was not able to write to the correct path");
		}

		//Testing readFile() by reading the data saved from the sub-test above
		employeePersistence = new EmployeePersistence(filepath);
		try {
			readEmployee = employeePersistence.readFile();
			assertSameEmployee(testData.getEmployee(), readEmployee, "The read employee was not the written employee.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("employeePersistence was not able to read from correct path");
		}


	}

	@Test
	public void testWriteAndReadWithSingleWorkPeriod() {
		//Testing with single workPeriod in workPeriods-hashmap
		//Testing writeFile() with valid filepath
		testData.getEmployee().addWorkPeriod(testData.getThisMonthWorkPeriod());
		employeePersistence = new EmployeePersistence(filepath);
		assertFileIsEmpty(filepath);
		try {
			employeePersistence.writeFile(testData.getEmployee());
			assertFileIsNotEmpty(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("employeePersistence was not able to write to the correct path");
		}

		//Testing readFile() by reading the data saved from the sub-test above
		employeePersistence = new EmployeePersistence(filepath);
		try {
			readEmployee = employeePersistence.readFile();
			assertSameEmployee(testData.getEmployee(), readEmployee, "The read employee was not the written employee.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("employeePersistence was not able to read from correct path");
		}
	}

	@Test
	public void testWriteAndReadWithMultipleWorkPeriods() {
		//Testing with multiple workPeriods in workPeriods-hashmap
		testData.getEmployee().addWorkPeriod(testData.getThisMonthWorkPeriod());
		WorkPeriod nextMonthWorkPeriod = new WorkPeriod(testData.getValidNextMonth(), testData.getValidYear(), testData.getValidHourlyWage());
		nextMonthWorkPeriod.addWork(testData.getWorkNextMonth());
		testData.getEmployee().addWorkPeriod(nextMonthWorkPeriod);
		employeePersistence = new EmployeePersistence(filepath);
		assertFileIsEmpty(filepath);
		try {
			employeePersistence.writeFile(testData.getEmployee());
			assertFileIsNotEmpty(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("employeePersistence was not able to write to the correct path");
		}

		//Testing readFile() by reading the data saved from the sub-test above
		employeePersistence = new EmployeePersistence(filepath);
		try {
			readEmployee = employeePersistence.readFile();
			assertSameEmployee(testData.getEmployee(), readEmployee, "The read employee was not the written employee.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail("employeePersistence was not able to read from correct path");
		}
	}

}

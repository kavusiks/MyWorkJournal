package myworkjournal.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static java.time.temporal.ChronoUnit.MINUTES;
import java.time.LocalDateTime;

public class WorkTest {
		private Work work;
		private TestData testData;


		@BeforeEach public void setUp() {
			testData = new TestData();
		}

	
	@Test
	public void testConstructor() {
		//Testing with valid inputs
		try {
			work = new Work(testData.getValidStartTime(), testData.getValidEndTime());
			assertNotNull(work, "Work should be assigned when Exceptions is not thrown");
			assertEquals(testData.getValidStartTime().truncatedTo(MINUTES), work.getStartTime(), "Expected starttime and actual starttime doesn't match.");
			assertEquals(testData.getValidEndTime().truncatedTo(MINUTES), work.getEndTime(),  "Expected starttime and actual starttime doesn't match.");
		} catch (Exception e) {
			fail("A valid work couldn't be created. Following starttime  " + testData.getValidStartTime() + " and following endtime " + testData.getValidEndTime() + " was used.");
		}

		//Testing with invalid inputs
		LocalDateTime invalidEndTimeBeforeStarTime = LocalDateTime.now().minusHours(2);
		assertThrows(IllegalArgumentException.class, () -> {
			work = new Work(testData.getValidStartTime(), invalidEndTimeBeforeStarTime);
		}, "Expected an IllegalArgumentException to be thrown when creating a work with invalid endTime that is before the startTime.");

		LocalDateTime invalidEndTimeLastingMoreThanOneDay = LocalDateTime.now().plusDays(3);
		assertThrows(IllegalArgumentException.class, () -> {
			work = new Work(testData.getValidStartTime(), invalidEndTimeLastingMoreThanOneDay);
		},"Expected an IllegalArgumentException to be thrown when creating an work with invalid endTime that makes the work lasts more than a day" );



		
	}
	
	@Test
	public void testGetHours() {
		work = new Work(testData.getValidStartTime(), testData.getValidEndTime());
		assertEquals(testData.getShiftDurationHours(), work.getShiftDurationInHours(),  "Expected shift duration and actual shift duration doesn't match.");
		LocalDateTime startTimeForShiftOverNight = LocalDateTime.parse("2021-04-03T23:00:00");
		LocalDateTime endTimeForShiftOverNight = startTimeForShiftOverNight.plusHours(testData.getShiftDurationHours());
		work = new Work(startTimeForShiftOverNight, endTimeForShiftOverNight);
		assertEquals(testData.getShiftDurationHours(), work.getShiftDurationInHours(), "Expected shift duration and actual shift duration doesn't match for overnight shifts.");

	}
	
	@Test
	public void testToString() {
		work = new Work(testData.getValidStartTime(), testData.getValidEndTime());
		assertEquals(testData.getValidStartTime().truncatedTo(MINUTES).toLocalDate() + " kl: " + testData.getValidStartTime().truncatedTo(MINUTES).toLocalTime() + " - " + testData.getValidEndTime().truncatedTo(MINUTES).toLocalDate() + " kl: " + testData.getValidEndTime().truncatedTo(MINUTES).toLocalTime(), work.toString());
	}

}

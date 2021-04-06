package myworkjournal.core;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WorkTest extends CoreTestData{
		private Work work;

	@BeforeEach
	public void setUp() {
		work = null;
	}
	
	@Test
	public void testConstructor() {
		LocalDateTime invalidEndTimeBeforeStarTime = LocalDateTime.now().minusHours(2);
		try {
			work = new Work(validStartTime, invalidEndTimeBeforeStarTime);
			fail("A invalid work has been created. Endtime " + invalidEndTimeBeforeStarTime + " has to be after Starttime " + validStartTime + ".");
		} catch (IllegalArgumentException e) {
			assertNull(work, "Work should not be assigned when Exceptions is thrown");
		}

		LocalDateTime invalidEndTimeLastingMoreThanOneDay = LocalDateTime.now().plusDays(3);
		try {
			work = new Work(validStartTime, invalidEndTimeLastingMoreThanOneDay);
			fail("A invalid work has been created. Endtime " + invalidEndTimeLastingMoreThanOneDay + " has to be after Starttime " + validStartTime + ".");
		} catch (IllegalArgumentException e) {
			assertNull(work, "Work should not be assigned when Exceptions is thrown");
		}

		try {
			work = new Work(validStartTime, validEndTime);
			assertNotNull(work, "Work should be assigned when Exceptions is not thrown");
			assertEquals(validStartTime.truncatedTo(MINUTES), work.getStartTime(), "Expected starttime and actual starttime doesn't match.");
			assertEquals(validEndTime.truncatedTo(MINUTES), work.getEndTime(),  "Expected starttime and actual starttime doesn't match.");
		} catch (Exception e) {
			fail("A valid work couldn't be created. Following starttime  " + validStartTime + " and following endtime " + validEndTime + " was used.");
		}


		
	}
	
	@Test
	public void testGetHours() {
		work = new Work(validStartTime, validEndTime);
		assertEquals(shiftDurationHours, work.getHours(),  "Expected shift duration and actual shift duration doesn't match.");
		LocalDateTime startTimeForShiftOverNight = LocalDateTime.parse("2021-04-03T23:00:00");
		LocalDateTime endTimeForShiftOverNight = startTimeForShiftOverNight.plusHours(shiftDurationHours);
		work = new Work(startTimeForShiftOverNight, endTimeForShiftOverNight);
		assertEquals(shiftDurationHours, work.getHours(), "Expected shift duration and actual shift duration doesn't match for overnight shifts.");

	}
	
	@Test
	public void testToString() {
		work = new Work(validStartTime, validEndTime);
		assertEquals(validStartTime.truncatedTo(MINUTES).toLocalDate() + " kl: " + validStartTime.truncatedTo(MINUTES).toLocalTime() + " - " + validEndTime.truncatedTo(MINUTES).toLocalDate() + " kl: " + validEndTime.truncatedTo(MINUTES).toLocalTime(), work.toString());
	}

}

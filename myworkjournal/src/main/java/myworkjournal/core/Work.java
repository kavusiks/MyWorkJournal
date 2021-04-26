package myworkjournal.core;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * This class represents a shift. A shift is made up by a startTime and a endTime. This is used to calculate the shift's duration.
 */
public class Work implements Comparable<Work> {
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;


  /**
   * The constructor to create an instance of Work. The shift has to start and end within 0-1 calender days.
   * The start- and endtime will be truncated to only show up to the specific minutes.
   *
   * @param startTime the shifts startingTime
   * @param endTime   the shifts endingTime
   * @throws IllegalArgumentException if the endTime is before the startTime
   *                                  or if the work lasts for more than 1 calender days.
   */
  public Work(LocalDateTime startTime, LocalDateTime endTime) throws IllegalArgumentException {
    if (!endTime.isAfter(startTime))
      throw new IllegalArgumentException("End time must be after start time!");
    if (DAYS.between(startTime, endTime) > 1)
      throw new IllegalArgumentException("A shift has to start and end during 0-1 calender day(s)!");
    this.startTime = startTime.truncatedTo(MINUTES);
    this.endTime = endTime.truncatedTo(MINUTES);
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  /**
   * Method used to calculate the shift's duration.
   *
   * @return the duration in hours
   */
  public double getShiftDurationInHours() {
    DecimalFormat df = new DecimalFormat("###.##");
    df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    return Double.parseDouble(df.format((double) Duration.between(getStartTime(), getEndTime()).toMinutes() / 60.00));
  }

  @Override public String toString() {
    return startTime.toLocalDate() + " kl: " + startTime.toLocalTime() + " - " + endTime.toLocalDate() + " kl: "
        + endTime.toLocalTime();
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.
   *
   * @param o the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   */
  @Override public int compareTo(Work o) {
    if (this.getStartTime().getYear() != o.getStartTime().getYear())
      return this.getStartTime().getYear() - o.getStartTime().getYear();
    if (this.getStartTime().getMonth() != o.getStartTime().getMonth())
      return this.getStartTime().getMonthValue() - o.getStartTime().getMonthValue();
    if (this.getStartTime().getDayOfMonth() != o.getStartTime().getDayOfMonth())
      return this.getStartTime().getDayOfMonth() - o.getStartTime().getDayOfMonth();
    if (this.getStartTime().getHour() != o.getStartTime().getHour())
      return this.getStartTime().getHour() - o.getStartTime().getHour();
    return this.getStartTime().getMinute() - o.getStartTime().getMinute();
  }


}

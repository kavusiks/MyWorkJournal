package myworkjournal.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Work {
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  //private int hours;

  public Work(LocalDateTime startTime, LocalDateTime endTime) throws IllegalArgumentException{
    //Er kun interessert i timer og minutter
    this.startTime = startTime.truncatedTo(ChronoUnit.MINUTES);
    this.endTime = endTime.truncatedTo(ChronoUnit.MINUTES);
    if(!endTime.isAfter(startTime))
      throw new IllegalArgumentException("End time must be after start time!");
    //this.hours = endTime.getHour() - startTime.getHour();
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public int getHours() {
    return endTime.getHour() - startTime.getHour();
    //return hours;
  }

  @Override public String toString() {
    return startTime.toLocalDate() + " kl: " + startTime.toLocalTime() + " - " + endTime.toLocalDate() + " kl: " + endTime.toLocalTime();
  }
}

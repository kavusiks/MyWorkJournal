package myworkjournal.core;

import java.time.LocalDateTime;

public class Work {
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  //private int hours;

  public Work(LocalDateTime startTime, LocalDateTime endTime) throws IllegalArgumentException{
    this.startTime = startTime;
    this.endTime = endTime;
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

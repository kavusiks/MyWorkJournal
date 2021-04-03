package myworkjournal.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

public class Work {
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  //private int hours;

  public Work(LocalDateTime startTime, LocalDateTime endTime) throws IllegalArgumentException{
    //Er kun interessert i timer og minutter
    if(!endTime.isAfter(startTime))
      throw new IllegalArgumentException("End time must be after start time!");
    //System.out.println("days between " + endTime + startTime +DAYS.between(startTime, endTime));
    if(DAYS.between(startTime, endTime) > 1) throw new IllegalArgumentException("A shift has to start and end during 0-1 day(s)!");
    this.startTime = startTime.truncatedTo(MINUTES);
    this.endTime = endTime.truncatedTo(MINUTES);
    //this.hours = endTime.getHour() - startTime.getHour();
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public int getHours() {
    int startHour = startTime.getHour();
    int endHour = endTime.getHour();
    if(startHour > endHour) endHour += 24;
    return endHour - startHour;
    //return hours;
  }

  @Override public String toString() {
    return startTime.toLocalDate() + " kl: " + startTime.toLocalTime() + " - " + endTime.toLocalDate() + " kl: " + endTime.toLocalTime();
  }
}

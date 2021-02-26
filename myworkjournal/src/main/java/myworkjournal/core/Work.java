package myworkjournal.core;

import java.time.LocalDateTime;

public class Work {
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private int hours;

  public Work(LocalDateTime startDate, int hours) {
    this.startTime = startDate;
    this.hours = hours;
    this.endTime = startTime.plusHours(hours);
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public int getHours() {
    return hours;
  }

  @Override public String toString() {
    return startTime.toLocalDate() + " kl: " + startTime.toLocalTime() + " - " + endTime.toLocalDate() + " kl: " + endTime.toLocalTime();
  }
}

package myworkjournal.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

public class Work implements Comparable<Work>{
  private LocalDateTime startTime;
  private LocalDateTime endTime;

  public Work(LocalDateTime startTime, LocalDateTime endTime) throws IllegalArgumentException{
    //Er kun interessert i timer og minutter
    if(!endTime.isAfter(startTime))
      throw new IllegalArgumentException("End time must be after start time!");
    //System.out.println("days between " + endTime + startTime +DAYS.between(startTime, endTime));
    if(DAYS.between(startTime, endTime) > 1) throw new IllegalArgumentException("A shift has to start and end during 0-1 day(s)!");
    this.startTime = startTime.truncatedTo(MINUTES);
    this.endTime = endTime.truncatedTo(MINUTES);
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
  }

  @Override public String toString() {
    return startTime.toLocalDate() + " kl: " + startTime.toLocalTime() + " - " + endTime.toLocalDate() + " kl: " + endTime.toLocalTime();
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.
   *
   * <p>The implementor must ensure
   * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
   * for all {@code x} and {@code y}.  (This
   * implies that {@code x.compareTo(y)} must throw an exception iff
   * {@code y.compareTo(x)} throws an exception.)
   *
   * <p>The implementor must also ensure that the relation is transitive:
   * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
   * {@code x.compareTo(z) > 0}.
   *
   * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
   * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
   * all {@code z}.
   *
   * <p>It is strongly recommended, but <i>not</i> strictly required that
   * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
   * class that implements the {@code Comparable} interface and violates
   * this condition should clearly indicate this fact.  The recommended
   * language is "Note: this class has a natural ordering that is
   * inconsistent with equals."
   *
   * <p>In the foregoing description, the notation
   * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
   * <i>signum</i> function, which is defined to return one of {@code -1},
   * {@code 0}, or {@code 1} according to whether the value of
   * <i>expression</i> is negative, zero, or positive, respectively.
   *
   * @param o the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException   if the specified object's type prevents it
   *                              from being compared to this object.
   */
  @Override public int compareTo(Work o) {
    if(this.getStartTime().getYear() != o.getStartTime().getYear()) return this.getStartTime().getYear() - o.getStartTime().getYear();
    if(this.getStartTime().getMonth() != o.getStartTime().getMonth()) return  this.getStartTime().getMonthValue() - o.getStartTime().getMonthValue();
    if(this.getStartTime().getDayOfMonth() != o.getStartTime().getDayOfMonth()) return this.getStartTime().getDayOfMonth() - o.getStartTime().getDayOfMonth();
    if(this.getStartTime().getHour() != o.getStartTime().getHour()) return this.getStartTime().getHour() - o.getStartTime().getHour();
    return this.getStartTime().getMinute() - o.getStartTime().getMinute();
  }
}

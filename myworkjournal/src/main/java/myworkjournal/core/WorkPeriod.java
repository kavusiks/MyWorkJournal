package myworkjournal.core;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WorkPeriod implements Iterable<Work>, Comparable<WorkPeriod> {
  public static final List<String> months = new ArrayList<>(Arrays
      .asList("januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november",
          "desember"));

  private String identifier;
  private LocalDate periodStartDate;
  private LocalDate periodEndDate;
  private int hourlyWage;



  private List<Work> periodWorkHistory = new ArrayList<>();

  public WorkPeriod(String month, int year, int hourlyWage) {
    String monthValue;
    if (months.contains(month.toLowerCase())) {
      monthValue = String.valueOf(months.indexOf(month.toLowerCase()) + 1);
    } else {
      throw new IllegalArgumentException(month + " is not written correctly or is not a valid month.");
    }
    if (year < (LocalDateTime.now().getYear() - 1) || year > (LocalDateTime.now().getYear() + 1))
      throw new IllegalArgumentException("Year can only be last, this or next year");
    if (hourlyWage <= 0)
      throw new IllegalArgumentException("HourlySalary cannot be in minus");
    identifier = month + "-" + year;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    if (monthValue.length() == 1)
      monthValue = "0" + monthValue;
    String startDate = "01/" + monthValue + "/" + year;
    this.periodStartDate = LocalDate.parse(startDate, formatter);
    this.periodEndDate = periodStartDate.withDayOfMonth(periodStartDate.getMonth().length(periodStartDate.isLeapYear()));
    this.hourlyWage = hourlyWage;
  }


  public String getIdentifier() {
    return identifier;
  }

  public LocalDate getPeriodStartDate() {
    return periodStartDate;
  }

  public LocalDate getPeriodEndDate() {
    return periodEndDate;
  }

  public List<Work> getPeriodWorkHistory() {
    return periodWorkHistory;
  }

  /**
   * Method used to check if the given instance of Work alreadt exists in the workPeriod.
   * This is done by comparing the start time and end time of the given work with all of the works in this workPeriod.
   *
   * @param workToCheck The work we want to check.
   * @return true if already exists.
   */
  private boolean checkWorkAlreadyAdded(Work workToCheck) {
    return periodWorkHistory.stream().anyMatch(work -> work.getStartTime().equals(workToCheck.getStartTime()) && work.getEndTime().equals(workToCheck.getEndTime()));
  }

  /**
   * Method used to check if a given Work is overlapping with some of the already existing works.
   * This is done by checking if the given work start within an already existing work, or if it ends within an already existing work.
   * And by checking if the given shifts cover an already existing shift.
   * @param workToCheck the work we want to check if it overlaps.
   * @return True it the work is overlapping.
   */
  private boolean checkIfAnyOverlappingWorksExists(Work workToCheck) {
    boolean startTimeIsOverlapping = periodWorkHistory.stream().anyMatch(work -> work.getStartTime().isBefore(workToCheck.getStartTime()) && work.getEndTime().isAfter(workToCheck.getStartTime()));
    boolean endTimeIsOverlapping = periodWorkHistory.stream().anyMatch(work -> work.getStartTime().isBefore(workToCheck.getEndTime()) && work.getEndTime().isAfter(workToCheck.getEndTime()));
    boolean coversExistingShift = periodWorkHistory.stream().anyMatch(work -> workToCheck.getStartTime().isBefore(work.getStartTime()) && workToCheck.getEndTime().isAfter(work.getStartTime()));
    return startTimeIsOverlapping || endTimeIsOverlapping || coversExistingShift;
  }


  public void addWork(Work work) throws IllegalArgumentException {

    if (checkWorkAlreadyAdded(work))
      throw new IllegalArgumentException("This work data already exists");
    if (checkIfAnyOverlappingWorksExists(work)) throw new IllegalArgumentException("This work is overlapping with an already existing work. You can only have one shift at a time!");
    if (work.getEndTime().toLocalDate().isBefore(getPeriodEndDate().plusDays(1))) {
      periodWorkHistory.add(work);
    } else {
      throw new IllegalArgumentException(
          "This shift is not in the month for this period. Choose a new period that matches the shift's endDate");
    }
    Collections.sort(periodWorkHistory);
  }

  public void removeWork(Work work) throws IllegalArgumentException {
    if (!periodWorkHistory.contains(work))
      throw new IllegalArgumentException("Workperiod does not contain the given work. Choose one of the existing works.");
    periodWorkHistory.remove(work);
  }

  public void setPeriodWorkHistory(List<Work> periodWorkHistory) {
    this.periodWorkHistory = periodWorkHistory;
  }


  public int getHourlyWage() {
    return this.hourlyWage;
  }

  public int getTotalHours() {
    int hours = 0;
    for (Work work : getPeriodWorkHistory()) {
      hours += work.getHours();
    }
    return hours;
  }

  public int getMonthSalary() {
    return this.hourlyWage * getTotalHours();
  }

  public static void main(String[] args) {
    Work work11 = new Work(LocalDateTime.now().minusHours(1), LocalDateTime.now());
    Work work12 = new Work(LocalDateTime.now().minusHours(1), LocalDateTime.now());
    Work work2 = new Work(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(1));
    Work work3 = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now());
    Work work4 = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(2));
    WorkPeriod wp = new WorkPeriod(months.get(LocalDate.now().getMonthValue() - 1), LocalDate.now().getYear(), 200);
    wp.addWork(work11);
    //wp.addWork(work12);
    //wp.addWork(work2);
    //wp.addWork(work3);
    //wp.addWork(work4);
    //System.out.println(wp.checkIfSameWork(work11, work12));
    System.out.println(wp.getPeriodWorkHistory().size());
    wp.removeWork(work11);
    System.out.println(wp.getPeriodWorkHistory().size());
  }

  @Override public Iterator<Work> iterator() {
    return periodWorkHistory.iterator();
  }

  @Override public String toString() {
    return getIdentifier();
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
  @Override public int compareTo(WorkPeriod o) {
    if (this.getPeriodStartDate().getYear() != o.getPeriodStartDate().getYear()) return this.getPeriodStartDate().getYear() - o.getPeriodStartDate().getYear();
    else return this.getPeriodStartDate().getMonth().getValue() - o.getPeriodStartDate().getMonth().getValue();
  }
}


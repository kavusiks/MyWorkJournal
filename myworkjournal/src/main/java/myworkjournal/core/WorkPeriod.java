package myworkjournal.core;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class represents a working month. A work month has an unique identifier, a startDate, a endDate og a hourly wage.
 */
public class WorkPeriod implements Iterable<Work>, Comparable<WorkPeriod> {
  public static final List<String> months = new ArrayList<>(Arrays
      .asList("januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november",
          "desember"));

  private String identifier;
  private LocalDate periodStartDate;
  private LocalDate periodEndDate;
  private int hourlyWage;

  private List<Work> periodWorkHistory = new ArrayList<>();

  //TODO: vurder behoved for at man kun kan lage for ifjor, i år og neste år. Hva om man skal legge til gammelt shit. Kanskje bare ha opptil i dag? Siden det er logg.

  /**
   * The constructor to create an instance of WorkPeriod. The month shall be written in english.
   * A WorkPeriod can only be created for a month in the last, this or next year.
   *
   * @param month      the month for the WorkPeriod
   * @param year       the year for the WorkPeriod
   * @param hourlyWage the hourly salary for works during this period
   * @throws IllegalArgumentException if month, year or hourlyWage are invalid.
   */
  public WorkPeriod(String month, int year, int hourlyWage) throws IllegalArgumentException {
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
    this.periodEndDate =
        periodStartDate.withDayOfMonth(periodStartDate.getMonth().length(periodStartDate.isLeapYear()));
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
   * Method used to check if the given instance of Work already exists in the WorkPeriod.
   * This is done by comparing the start time and end time of the given work with all of the works in this workPeriod.
   *
   * @param workToCheck The work we want to check.
   * @return true if the work already exists.
   */
  private boolean checkWorkAlreadyAdded(Work workToCheck) {
    return periodWorkHistory.stream().anyMatch(
        work -> work.getStartTime().equals(workToCheck.getStartTime()) && work.getEndTime()
            .equals(workToCheck.getEndTime()));
  }

  /**
   * Method used to check if a given Work is overlapping with some of the already existing works.
   * This is done by checking if the given work start within an already existing work, or if it ends within an already existing work.
   * And by checking if the given shift covers an already existing shift.
   *
   * @param workToCheck The work we want to check if it overlaps.
   * @return True it the work is overlapping.
   */
  private boolean checkIfAnyOverlappingWorksExists(Work workToCheck) {
    boolean startTimeIsOverlapping = periodWorkHistory.stream().anyMatch(
        work -> work.getStartTime().isBefore(workToCheck.getStartTime()) && work.getEndTime()
            .isAfter(workToCheck.getStartTime()));
    boolean endTimeIsOverlapping = periodWorkHistory.stream().anyMatch(
        work -> work.getStartTime().isBefore(workToCheck.getEndTime()) && work.getEndTime()
            .isAfter(workToCheck.getEndTime()));
    boolean coversExistingShift = periodWorkHistory.stream().anyMatch(
        work -> workToCheck.getStartTime().isBefore(work.getStartTime()) && workToCheck.getEndTime()
            .isAfter(work.getStartTime()));
    return startTimeIsOverlapping || endTimeIsOverlapping || coversExistingShift;
  }


  /**
   * The method user to add work to the WorkPeriod.
   *
   * @param work The work to add
   * @throws IllegalArgumentException if the work already exists, is overlapping with another existing work
   *                                  or isn't during this WorkPeriod.
   */
  public void addWork(Work work) throws IllegalArgumentException {
	  
	if (work == null)
	  throw new IllegalArgumentException("Can't add null-object");
    if (checkWorkAlreadyAdded(work))
      throw new IllegalArgumentException("This work data already exists");
    if (checkIfAnyOverlappingWorksExists(work))
      throw new IllegalArgumentException(
          "This work is overlapping with an already existing work. You can only have one shift at a time!");
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
      throw new IllegalArgumentException(
          "WorkPeriod does not contain the given work. Choose one of the existing works.");
    periodWorkHistory.remove(work);
  }

  public void setPeriodWorkHistory(List<Work> periodWorkHistory) {
    this.periodWorkHistory = periodWorkHistory;
  }


  public int getHourlyWage() {
    return this.hourlyWage;
  }

  /**
   * Calculates the total hours of work during this WorkPeriod by sum up shift duration hours
   * for each shift in this WorkPeriod.
   *
   * @return the total work hours.
   */
  public double getTotalHours() {
    double hours = 0;
    for (Work work : getPeriodWorkHistory()) {
      hours += work.getShiftDurationInHours();
    }
    return hours;
  }

  public double getMonthSalary() {
    return this.hourlyWage * getTotalHours();
  }

  //TODO: sjekk om dette trengs, og evt nevn hvorfor dette brukes og hvor.

  /**
   * Iterator used to iterate over the all works directly from the WorkPeriod.
   *
   * @return the iterator of the list with work history.
   */
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
   * @param o the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   */
  @Override public int compareTo(WorkPeriod o) {
    if (this.getPeriodStartDate().getYear() != o.getPeriodStartDate().getYear())
      return this.getPeriodStartDate().getYear() - o.getPeriodStartDate().getYear();
    else
      return this.getPeriodStartDate().getMonth().getValue() - o.getPeriodStartDate().getMonth().getValue();
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
}


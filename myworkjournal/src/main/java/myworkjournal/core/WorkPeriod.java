package myworkjournal.core;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WorkPeriod implements Iterable<Work> {
  public static final List<String> months = new ArrayList<>(
      Arrays.asList("januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember"));

  private String identifier;
  private LocalDate periodStartDate;
  private LocalDate periodEndDate;
  private int hourlyWage;



  private Collection<Work> periodWorkHistory = new ArrayList<>();

  public WorkPeriod(String month, int year, int hourlyWage) {
    String monthValue;
    if(months.contains(month.toLowerCase())) {
      monthValue = String.valueOf(months.indexOf(month.toLowerCase()) + 1);
    }
    else {
      throw new IllegalArgumentException(month + " is not written correctly or is not a valid month.");
    }
    if (year < (LocalDateTime.now().getYear() - 1) || year > (LocalDateTime.now().getYear() + 1)) throw new IllegalArgumentException("Year can only be last, this or next year");
    if (hourlyWage<=0) throw new IllegalArgumentException("HourlySalary cannot be in minus");
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

  public Collection<Work> getPeriodWorkHistory() {
    return periodWorkHistory;
  }

//TODO: hvorfor ikke bare ta og sammenlige localdatetime direkte?
  /**
   * Method used to check if the two given instances for Work are the same.
   * This is done by comparing the start time and end time of each work.
   * The comparison is done by first comparing the date, and the following start hour and minute.
   * @param work1 The first instance of Work
   * @param work2 The second instance of Work
   * @return true if they are the same
   */
  private boolean checkIfSameWork(Work work1, Work work2) {
    if (work1.getStartTime().toLocalDate().equals(work2.getStartTime().toLocalDate())) {
      if((work1.getStartTime().getHour() == work2.getStartTime().getHour()) && (work1.getStartTime().getMinute() == work2.getStartTime().getMinute()))
        if ((work1.getEndTime().toLocalDate().equals(work2.getEndTime().toLocalDate()))){
          return work1.getEndTime().getHour() == work2.getEndTime().getHour() && work1.getEndTime().getMinute() == work2
              .getEndTime().getMinute();
        }
    }
    return false;
  }

  private boolean checkWorkAlreadyAdded(Work workToCheck) {
    return periodWorkHistory.stream().anyMatch(work -> checkIfSameWork(work, workToCheck));
  }


  public void addWork(Work work) throws IllegalArgumentException {

    if (checkWorkAlreadyAdded(work))
      throw new IllegalArgumentException("This work data already exists");
    if (work.getEndTime().toLocalDate().isBefore(getPeriodEndDate().plusDays(1))) {
      periodWorkHistory.add(work);
    } else {
      throw new IllegalArgumentException("This shift is not in the month for this period. Choose a new period that matches the shift's endDate");
    }
  }

  public void setPeriodWorkHistory(Collection<Work> periodWorkHistory) {
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
    Work work11 = new Work(LocalDateTime.now().minusHours(2), LocalDateTime.now());
    Work work12 = new Work(LocalDateTime.now().minusHours(2), LocalDateTime.now());
    Work work2 = new Work(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(1));
    Work work3 = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now());
    Work work4 = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(2));
    WorkPeriod wp = new WorkPeriod("mars", 2021, 200);
    wp.addWork(work11);
    //wp.addWork(work12);
    wp.addWork(work2);
    wp.addWork(work3);
    wp.addWork(work4);
    //System.out.println(wp.checkIfSameWork(work11, work12));
}

  @Override public Iterator<Work> iterator() {
    return periodWorkHistory.iterator();
  }
}


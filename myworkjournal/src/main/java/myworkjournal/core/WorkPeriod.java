package myworkjournal.core;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;

public class WorkPeriod {
  private LocalDate monthStartDate;


  private LocalDate monthEndDate;
  private int hourlyWage;


  private Collection<Work> periodWorkHistory = new ArrayList<Work>();

  public WorkPeriod(LocalDate startDate, int hourlyWage) {
    String month = Integer.toString(startDate.getMonthValue());
    String year = Integer.toString(startDate.getYear());
    if (month.length() == 1)
      month = "0" + month;
    this.monthStartDate = LocalDate.parse(year + "-" + month + "-01");
    this.monthEndDate = startDate.withDayOfMonth(startDate.getMonth().length(startDate.isLeapYear()));
    //YearMonth startMonth = YearMonth.of(startDate.getYear(), startDate.getMonthValue());
    //this.monthEndDate = startMonth.atDay(4);
    //this.monthEndDate = LocalDate.now();
    this.hourlyWage = hourlyWage;
    //this.monthEndDate = monthStartDate;// skal være last date of the month, søk opp på nett
    System.out.println(monthStartDate);
    System.out.println("jj" + monthEndDate);
  }


  public String getIdentifier() {
    String month = Integer.toString(monthStartDate.getMonthValue());
    String year = Integer.toString(monthStartDate.getYear());
    return month + "-" + year;
  }

  public LocalDate getMonthStartDate() {
    return monthStartDate;
  }

  public LocalDate getMonthEndDate() {
    return monthEndDate;
  }

  public Collection<Work> getPeriodWorkHistory() {
    return periodWorkHistory;
  }


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
          if((work1.getEndTime().getHour() == work2.getEndTime().getHour()) && (work1.getEndTime().getMinute() == work2.getEndTime().getMinute()))
          return true;
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
    if (work.getEndTime().toLocalDate().isBefore(getMonthEndDate()) && work.getStartTime().toLocalDate().isAfter(getMonthStartDate())) {
      System.out.println(work.getEndTime());
      System.out.println(work.getEndTime().toLocalDate().isBefore(getMonthEndDate()));
      System.out.println(work.getStartTime());
      System.out.println(work.getStartTime().toLocalDate().isAfter(getMonthStartDate()));
      periodWorkHistory.add(work);
    } else {
      throw new IllegalArgumentException("This shift is not in the month for this period. Create a new period");
    }
  }

  public void setPeriodWorkHistory(Collection<Work> periodWorkHistory) {
    this.periodWorkHistory = periodWorkHistory;
  }

  public Collection<Work> getWorkHistory() {
    return this.periodWorkHistory;
  }

  public int getHourlyWage() {
    return this.hourlyWage;
  }

  public int getTotalHours() {
    int hours = 0;
    for (Work work : getWorkHistory()) {
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
    WorkPeriod wp = new WorkPeriod(LocalDate.now(), 200);
    wp.addWork(work11);
    //wp.addWork(work12);
    wp.addWork(work2);
    wp.addWork(work3);
    wp.addWork(work4);
    System.out.println(wp.checkIfSameWork(work11, work12));
}
}


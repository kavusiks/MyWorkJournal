package myworkjournal.core;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WorkPeriod implements Iterable<Work> {
  public static final List<String> months = new ArrayList<String>(
      Arrays.asList("januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember"));

  private String identifier;
  private LocalDate periodStartDate;
  private LocalDate periodEndDate;
  private int hourlyWage;



  private Collection<Work> periodWorkHistory = new ArrayList<Work>();

  public WorkPeriod(String month, int year, int hourlyWage) {
    identifier = month + "-" + year;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
    String monthValue = String.valueOf(months.indexOf(month) + 1);
    if (monthValue.length() == 1)
      monthValue = "0" + monthValue;
    String startDate = "01/" + monthValue + "/" + year;
    this.periodStartDate = LocalDate.parse(startDate, formatter);
    this.periodEndDate = periodStartDate.withDayOfMonth(periodStartDate.getMonth().length(periodStartDate.isLeapYear()));
    //YearMonth startMonth = YearMonth.of(startDate.getYear(), startDate.getMonthValue());
    //this.monthEndDate = startMonth.atDay(4);
    //this.monthEndDate = LocalDate.now();
    this.hourlyWage = hourlyWage;
    //this.monthEndDate = monthStartDate;// skal være last date of the month, søk opp på nett
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
    if (work.getEndTime().toLocalDate().isBefore(getPeriodEndDate().plusDays(1)) && work.getStartTime().toLocalDate().isAfter(
        getPeriodStartDate().minusDays(1))) {
      periodWorkHistory.add(work);
    } else {
      System.out.println("prøver  adde work"+ work.toString());
      System.out.println("Kanskje fordi periodstartdate er: " + getPeriodStartDate());
      System.out.println("og sluttdate er: " + getPeriodEndDate());
      throw new IllegalArgumentException("This shift is not in the month for this period. Create a new period mathing the shift's enddate");
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


package myworkjournal.core;



import java.time.LocalDate;
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
    if(month.length()==1) month="0"+month;
    this.monthStartDate =LocalDate.parse(year+"-"+month+"-01");
    this.monthEndDate = startDate.withDayOfMonth(startDate.getMonth().length(startDate.isLeapYear()));
    //YearMonth startMonth = YearMonth.of(startDate.getYear(), startDate.getMonthValue());
    //this.monthEndDate = startMonth.atDay(4);
    //this.monthEndDate = LocalDate.now();
    this.hourlyWage = hourlyWage;
    //this.monthEndDate = monthStartDate;// skal være last date of the month, søk opp på nett
    System.out.println(monthStartDate);
    System.out.println("jj"+monthEndDate);
  }


  public String getIdentifier(){
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

  public void addWork(Work work) throws IllegalArgumentException{
    if(work.getEndTime().toLocalDate().isBefore(getMonthEndDate()) && work.getStartTime().toLocalDate().isAfter(
        getMonthStartDate())) {
      System.out.println(work.getEndTime());
      System.out.println(work.getEndTime().toLocalDate().isBefore(getMonthEndDate()));
      System.out.println(work.getStartTime());
      System.out.println(work.getStartTime().toLocalDate().isAfter(getMonthStartDate()));
      periodWorkHistory.add(work);
    }
    else {

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
    for (Work work: getWorkHistory()) {
      hours+= work.getHours();
    }
    return hours;
  }

  public int getMonthSalary() {
    return this.hourlyWage*getTotalHours();
  }
}

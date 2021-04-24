package myworkjournal.core;

import java.time.LocalDateTime;

public class TestData {
  //Testdata used several times in both core and persistence test
  private LocalDateTime validStartTime = LocalDateTime.now();
  private int shiftDurationHours = 5;
  private LocalDateTime validEndTime = LocalDateTime.now().plusHours(shiftDurationHours);
  private Work workThisMonth = new Work(validStartTime, validEndTime);
  private Work workThisMonth2 = new Work(validStartTime.minusDays(1), validEndTime.minusDays(1));
  private Work workThisMonth3 = new Work(validStartTime.minusDays(2), validEndTime.minusDays(2));
  private LocalDateTime validStartTimeNextMonth = validStartTime.plusMonths(1);
  private LocalDateTime validEndTimeNextMonth = validStartTimeNextMonth.plusHours(shiftDurationHours);
  private Work workNextMonth = new Work(validStartTimeNextMonth,validEndTimeNextMonth);
  private int validYear = validStartTime.getYear();
  private  int validHourlyWage = 150;
  private String validThisMonth = WorkPeriod.months.get(LocalDateTime.now().getMonth().getValue() - 1);
  private String validNextMonth = WorkPeriod.months.get(LocalDateTime.now().getMonth().getValue());
  private WorkPeriod thisMonthWorkPeriod = new WorkPeriod(validThisMonth, validYear, validHourlyWage);
  private Employee employee = new Employee("Ola");

  public LocalDateTime getValidStartTime() {
    return validStartTime;
  }

  public int getShiftDurationHours() {
    return shiftDurationHours;
  }

  public LocalDateTime getValidEndTime() {
    return validEndTime;
  }

  public Work getWorkThisMonth() {
    return workThisMonth;
  }

  public Work getWorkThisMonth2() {
    return workThisMonth2;
  }

  public Work getWorkThisMonth3() {
    return workThisMonth3;
  }

  public Work getWorkNextMonth() {
    return workNextMonth;
  }

  public int getValidYear() {
    return validYear;
  }

  public int getValidHourlyWage() {
    return validHourlyWage;
  }

  public String getValidThisMonth() {
    return validThisMonth;
  }

  public String getValidNextMonth() {
    return validNextMonth;
  }

  public WorkPeriod getThisMonthWorkPeriod() {
    return thisMonthWorkPeriod;
  }

  public Employee getEmployee() {
    return employee;
  }

}

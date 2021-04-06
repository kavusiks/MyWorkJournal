package myworkjournal.core;

import java.time.LocalDateTime;

public class CoreTestData {
  //Data that are public are used in persistenceTests
  LocalDateTime validStartTime = LocalDateTime.now();
  int shiftDurationHours = 5;
  LocalDateTime validEndTime = LocalDateTime.now().plusHours(shiftDurationHours);
  public Work workThisMonth = new Work(validStartTime, validEndTime);
  public Work workThisMonth2 = new Work(validStartTime.minusDays(1), validEndTime.minusDays(1));
  public Work workThisMonth3 = new Work(validStartTime.minusDays(2), validEndTime.minusDays(2));
  LocalDateTime validStartTimeNextMonth = validStartTime.plusMonths(1);
  LocalDateTime validEndTimeNextMonth = validStartTimeNextMonth.plusHours(shiftDurationHours);
  Work workNextMonth = new Work(validStartTimeNextMonth,validEndTimeNextMonth);
  int validYear = validStartTime.getYear();
  int validHourlyWage = 150;
  String validThisMonth = WorkPeriod.months.get(LocalDateTime.now().getMonth().getValue() - 1);
  String validNextMonth = WorkPeriod.months.get(LocalDateTime.now().getMonth().getValue());
  public WorkPeriod thisMonthWorkPeriod = new WorkPeriod(validThisMonth, validYear, validHourlyWage);
  Employee employee = new Employee("Ola");



}

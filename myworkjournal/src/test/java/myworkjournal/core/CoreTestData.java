package myworkjournal.core;

import java.time.LocalDateTime;

public class CoreTestData {
  LocalDateTime validStartTime = LocalDateTime.now();
  int shiftDurationHours = 5;
  LocalDateTime validEndTime = LocalDateTime.now().plusHours(shiftDurationHours);
  Work workThisMonth = new Work(validStartTime, validEndTime);
  Work workThisMonth2 = new Work(validStartTime.minusDays(1), validEndTime.minusDays(1));
  Work workThisMonth3 = new Work(validStartTime.minusDays(2), validEndTime.minusDays(2));
  LocalDateTime validStartTimeNextMonth = validStartTime.plusMonths(1);
  LocalDateTime validEndTimeNextMonth = validStartTimeNextMonth.plusHours(shiftDurationHours);
  Work workNextMonth = new Work(validStartTimeNextMonth,validEndTimeNextMonth);
  int validYear = validStartTime.getYear();
  int validHourlyWage = 150;
  String validThisMonth = WorkPeriod.months.get(LocalDateTime.now().getMonth().getValue() - 1);
  String validNextMonth = WorkPeriod.months.get(LocalDateTime.now().getMonth().getValue());
  WorkPeriod thisMonthWorkPeriod = new WorkPeriod(validThisMonth, validYear, validHourlyWage);
  Employee employee = new Employee("Ola");

}

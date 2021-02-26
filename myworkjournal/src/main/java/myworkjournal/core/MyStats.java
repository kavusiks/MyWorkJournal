package myworkjournal.core;

import java.util.Collection;

public class MyStats {
  private Employee employee;

  public MyStats(Employee employee) {
    this.employee = employee;
  }

  public int getTotalSalary() {
    Collection<WorkPeriod> workPeriods = employee.getWorkPeriods().values();
    int salary= 0;
    for (WorkPeriod workPeriod : workPeriods) {
      salary += workPeriod.getMonthSalary();
    }
    return salary;
  }

}

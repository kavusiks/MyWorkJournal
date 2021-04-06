package myworkjournal.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.OptionalInt;

public class MyStats {
  private Employee employee;

  public MyStats(Employee employee) {
    this.employee = employee;
  }

  public int getTotalSalary() {
    return employee.getWorkPeriods().values().stream().mapToInt(WorkPeriod::getMonthSalary).sum();
  }

  public WorkPeriod getBestPaidWorkPeriod() {
    //optinalInt test = employee.getWorkPeriods().values().stream().mapToInt(WorkPeriod::getMonthSalary).max();

    WorkPeriod highestPaidWorkPeriod = null;
    int salaryForHighestPaidWorkPeriod = 0;
    for (WorkPeriod workPeriod : employee.getWorkPeriods().values()) {
      if (workPeriod.getMonthSalary() > salaryForHighestPaidWorkPeriod) {
        salaryForHighestPaidWorkPeriod = workPeriod.getMonthSalary();
        highestPaidWorkPeriod = workPeriod;
      }
    }
    return highestPaidWorkPeriod;
  }

  //TODO: gjør om til smartere metode som tar inn input
/*
  public double getAverage(Method method) {
    int total =
        employee.getWorkPeriods().values().stream().mapToInt(workPeriod -> {
          try {
            return (method.invoke(workPeriod));
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
        })
            .sum();
    return (double)total / (double)employee.getWorkPeriods().values().size();
  }

 */

  public double getAverageShiftAmount() {
    int totalShiftAmounts =
        employee.getWorkPeriods().values().stream().mapToInt(workPeriod -> (workPeriod.getPeriodWorkHistory().size()))
            .sum();
    return (double)totalShiftAmounts / (double)employee.getWorkPeriods().values().size();
  }

  public double getAverageWorkHours() {
    int totalHours =
        employee.getWorkPeriods().values().stream().mapToInt(WorkPeriod::getTotalHours)
            .sum();
    return (double)totalHours / (double)employee.getWorkPeriods().values().size();
  }

  public double getAverageSalary() {
    int totalSalary =
        employee.getWorkPeriods().values().stream().mapToInt(WorkPeriod::getMonthSalary)
            .sum();
    return (double)totalSalary / (double)employee.getWorkPeriods().values().size();
  }

  public double getAverageHourlyWage() {

    int totalHourlyWage =
        employee.getWorkPeriods().values().stream().mapToInt(WorkPeriod::getHourlyWage)
            .sum();
    return (double)totalHourlyWage / (double)employee.getWorkPeriods().values().size();
  }

}

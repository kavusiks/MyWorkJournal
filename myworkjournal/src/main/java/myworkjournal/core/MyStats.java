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

  public double getTotalSalary() {
    return employee.getWorkPeriods().stream().mapToDouble(WorkPeriod::getMonthSalary).sum();
  }

  public WorkPeriod getBestPaidWorkPeriod() {
    //optinalInt test = employee.getWorkPeriods().stream().mapToInt(WorkPeriod::getMonthSalary).max();

    WorkPeriod highestPaidWorkPeriod = null;
    double salaryForHighestPaidWorkPeriod = 0;
    for (WorkPeriod workPeriod : employee.getWorkPeriods()) {
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
        employee.getWorkPeriods().stream().mapToInt(workPeriod -> {
          try {
            return (method.invoke(workPeriod));
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
        })
            .sum();
    return (double)total / (double)employee.getWorkPeriods().size();
  }

 */

  public double getAverageShiftAmount() {
    int totalShiftAmounts =
        employee.getWorkPeriods().stream().mapToInt(workPeriod -> (workPeriod.getPeriodWorkHistory().size()))
            .sum();
    return (double)totalShiftAmounts / (double)employee.getWorkPeriods().size();
  }

  public double getAverageWorkHours() {
    double totalHours =
        employee.getWorkPeriods().stream().mapToDouble(WorkPeriod::getTotalHours)
            .sum();
    return totalHours / (double)employee.getWorkPeriods().size();
  }

  public double getAverageSalary() {
    double totalSalary =
        employee.getWorkPeriods().stream().mapToDouble(WorkPeriod::getMonthSalary)
            .sum();
    return totalSalary / (double)employee.getWorkPeriods().size();
  }

  public double getAverageHourlyWage() {

    int totalHourlyWage =
        employee.getWorkPeriods().stream().mapToInt(WorkPeriod::getHourlyWage)
            .sum();
    return (double)totalHourlyWage / (double)employee.getWorkPeriods().size();
  }

}


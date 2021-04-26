package myworkjournal.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents an Employee. An Employee has a name and a list of WorkPeriods.
 */
public class Employee implements Iterable<WorkPeriod> {

  private final String name;
  private List<WorkPeriod> workPeriods = new ArrayList<>();


  /**
   * The constructor to create an instance of Employee if the name fulfills the criteria.
   *
   * @param name the name of the Employee
   * @throws IllegalArgumentException if the name doesn't only contain letters.
   */
  public Employee(String name) throws IllegalArgumentException {
    if (name.matches("^[a-zA-ZæøåÆØÅ\\p{L}]+$")) {
      this.name = name;
    } else {
      throw new IllegalArgumentException("Brukernavn kan bare inneholde bokstaver");
    }
  }

  /**
   * Constructor user to recreate an instance of Employee from saved data.
   *
   * @param name        the name of the saved Employee
   * @param workPeriods the workPeriods of the saved Employee
   * @throws IllegalArgumentException if the name doesn't only contain letters.
   */
  public Employee(String name, List<WorkPeriod> workPeriods) throws IllegalArgumentException {
    this(name);
    this.workPeriods = workPeriods;
  }


  public List<WorkPeriod> getWorkPeriods() {
    return this.workPeriods;
  }


  /**
   * Method used to add new workPeriod. The workPeriod will be sorted
   * at the end to make sure that they are in the corrected order after
   * this newly added change.
   *
   * @param workPeriod the workPeriod we want to add.
   * @throws IllegalArgumentException if the workPeriod already exists or is null.
   */
  public void addWorkPeriod(WorkPeriod workPeriod) throws IllegalArgumentException {
    if (workPeriods.stream().anyMatch(p -> p.getIdentifier().equals(workPeriod.getIdentifier()))) {
      throw new IllegalArgumentException("Work month already exists");
    } else if (workPeriod == null) {
      throw new IllegalArgumentException("Can't add null-object");
    } else {
      workPeriods.add(workPeriod);
    }
    Collections.sort(workPeriods);
  }


  /**
   * Method used to remove an existing WorkPeriod from the Employee's workPeriods.
   *
   * @param workPeriod the WorkPeriod we want to remove.
   * @throws IllegalArgumentException if the Employee doesn't have the WorkPeriod we want to remove or is null.
   */
  public void removeWorkPeriod(WorkPeriod workPeriod) throws IllegalArgumentException {
    if (workPeriod == null)
      throw new IllegalArgumentException(
          "The employee's workPeriods doesn't contain the given workPeriod. Choose on of the existing workPeriods");
    if (!workPeriods.contains(workPeriod))
      throw new IllegalArgumentException(
          "The employee's workPeriods doesn't contain the given workPeriod. Choose on of the existing workPeriods");
    workPeriods.remove(workPeriod);
  }


  public String getName() {
    return this.name;
  }

  @Override public String toString() {
    return "Employee{" + "name='" + name + '\'' + ", workPeriods=" + workPeriods + '}';
  }


  /**
   * Iterator used to iterate over the all workPeriods directly from the Employee.
   *
   * @return the iterator of the list with WorkPeriod.
   */
  @Override public Iterator<WorkPeriod> iterator() {
    return workPeriods.iterator();
  }

  /**
   * Used to sum up salary for every WorkPeriods.
   *
   * @return Employee's total salary.
   */
  public double getTotalSalary() {
    return this.getWorkPeriods().stream().mapToDouble(WorkPeriod::getMonthSalary).sum();
  }

  public double getTotalWorkHours() {
    return this.getWorkPeriods().stream().mapToDouble((WorkPeriod::getTotalHours)).sum();
  }


  public WorkPeriod getBestPaidWorkPeriod() {

    return this.getWorkPeriods().stream().min((o1, o2) -> (int) (o2.getMonthSalary() - o1.getMonthSalary()))
        .orElse(null);
  }

  /**
   * Method used to calculate different average amounts. This private method is
   * used by other getAverage.... methods.
   *
   * @param total The total amount we want to divide on the amount of WorkPeriods.
   * @return the average amount of the given sum.
   */
  private double getAveragePerWorkPeriod(double total) {
    return total / (double) this.getWorkPeriods().size();
  }

  public double getAverageShiftAmount() {
    int totalShiftAmounts =
        this.getWorkPeriods().stream().mapToInt(workPeriod -> (workPeriod.getPeriodWorkHistory().size())).sum();
    return getAveragePerWorkPeriod(totalShiftAmounts);

  }

  public double getAverageWorkHours() {
    double totalHours = this.getWorkPeriods().stream().mapToDouble(WorkPeriod::getTotalHours).sum();
    return getAveragePerWorkPeriod(totalHours);
  }

  public double getAverageSalary() {
    double totalSalary = this.getWorkPeriods().stream().mapToDouble(WorkPeriod::getMonthSalary).sum();
    return getAveragePerWorkPeriod(totalSalary);
  }

  public double getAverageHourlyWage() {
    int totalHourlyWage = this.getWorkPeriods().stream().mapToInt(WorkPeriod::getHourlyWage).sum();
    return getAveragePerWorkPeriod(totalHourlyWage);
  }

}


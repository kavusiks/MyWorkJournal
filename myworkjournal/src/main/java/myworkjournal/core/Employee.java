package myworkjournal.core;

import java.util.*;

/**
 * This class represents an Employee. An Employee has a name and a list of WorkPeriods.
 */
public class Employee implements Iterable<WorkPeriod> {

    private String name;
    private List<WorkPeriod> workPeriods = new ArrayList<>();


    /**
     * The constructor to create an instance of Employee if the name fulfills the criteria.
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
     * @param name the name of the saved Employee
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
     * Method used to add new workPeriod. If the workPeriod already exists,
     * their periodWorkHistory will be merged. The workPeriod will be sorted
     * at the end to make sure that they are in the corrected order after
     * this newly added change.
     *
     * @param workPeriod the workPeriod we want to add.
     */
    public void addWorkPeriod(WorkPeriod workPeriod) {
        if(workPeriods.stream().anyMatch(p -> p.getIdentifier().equals(workPeriod.getIdentifier()))) {
            WorkPeriod existingWorkPeriod = workPeriods.stream().filter(existingWP -> existingWP.getIdentifier().equals(workPeriod.getIdentifier())).findAny().orElseThrow();
            workPeriods.remove(existingWorkPeriod);
            workPeriods.add(mergeTwoWorkPeriods(existingWorkPeriod, workPeriod));
        }
        else {
            workPeriods.add(workPeriod);
        }
        Collections.sort(workPeriods);
    }


    /**
     * Method used to remove an existing WorkPeriod from the Employee's workPeriods.
     * @param workPeriod the WorkPeriod we want to remove.
     * @throws IllegalArgumentException if the Employee doesn't have the WorkPeriod we want to remove.
     */
    public void removeWorkPeriod(WorkPeriod workPeriod) throws IllegalArgumentException {
        if(workPeriod== null) throw new IllegalArgumentException("The employee's workPeriods doesn't contain the given workPeriod. Choose on of the existing workPeriods");
        if(!workPeriods.contains(workPeriod)) throw new IllegalArgumentException("The employee's workPeriods doesn't contain the given workPeriod. Choose on of the existing workPeriods");
        workPeriods.remove(workPeriod);
    }

    /**
     * Method used to mergeTwoWorkPeriods by merging their PeriodWorkHistory.
     * This method is used when adding an already existing WorkPeriod to an Employee.
     * @param workPeriod1 WorkPeriod to merge
     * @param workPeriod2 WorkPeriod to merge
     * @return the merged WorkPeriod
     */
    private WorkPeriod mergeTwoWorkPeriods(WorkPeriod workPeriod1, WorkPeriod workPeriod2) {
        List<Work> workHistory1 = workPeriod1.getPeriodWorkHistory();
        List<Work> workHistory2 = workPeriod2.getPeriodWorkHistory();
        for (Work work : workHistory2) {
            if (!workHistory1.contains(work)) {
                workHistory1.add(work);
            }
        }
        workPeriod1.setPeriodWorkHistory(workHistory1);
        return workPeriod1;
    }

    public String getName(){
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
}


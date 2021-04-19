package myworkjournal.core;

import java.util.*;

public class Employee implements Iterable<WorkPeriod> {

    private String name;
    //private HashMap<String, WorkPeriod> workPeriods; //Collection eller array?
    private List<WorkPeriod> workPeriods = new ArrayList<>();

    public Employee(String name) {
        setName(name);
        //todo: hvorfor ha setname?
    }

    public Employee(String name, List<WorkPeriod> workPeriods) {
        this.name = name;
        this.workPeriods = workPeriods;
    }

    public void setName(String name) throws IllegalArgumentException{
        if (!name.matches("^[a-zA-ZæøåÆØÅ\\p{L}]+$")) {
            throw new IllegalArgumentException("Brukernavn kan bare inneholde bokstaver");
            // Legg til try catch...?
        }
        this.name = name;
    }

    /*public HashMap<String, WorkPeriod> getWorkPeriods() {
        return this.workPeriods;
    }*/
    public List<WorkPeriod> getWorkPeriods() {
        return this.workPeriods;
    }

    public void addWorkPeriod(WorkPeriod workPeriod) {
        if(workPeriods.stream().anyMatch(p -> p.getIdentifier().equals(workPeriod.getIdentifier()))) {
            WorkPeriod existingWorkPeriod = workPeriods.stream().filter(existingWP -> existingWP.getIdentifier().equals(workPeriod.getIdentifier())).findAny().orElseThrow();
            workPeriods.remove(existingWorkPeriod);
            workPeriods.add(mergeTwoWorkPeriods(existingWorkPeriod, workPeriod));
        }
        /*
        if(workPeriods.containsKey(workPeriod.getIdentifier())) {

            workPeriods.put(workPeriod.getIdentifier(), mergeTwoWorkPeriods(workPeriods.get(workPeriod.getIdentifier()), workPeriod));
        }
        */
        else {
            workPeriods.add(workPeriod);
        //workPeriods.put(workPeriod.getIdentifier(), workPeriod);
        }

        Collections.sort(workPeriods);
    }
/*
    public void removeWorkPeriod(WorkPeriod workPeriod) throws IllegalArgumentException {
        if(workPeriod== null) throw new IllegalArgumentException("The employee's workPeriods doesn't contain the given workPeriod. Choose on of the existing workPeriods");
        if(!workPeriods.containsKey(workPeriod.getIdentifier())) throw new IllegalArgumentException("The employee's workPeriods doesn't contain the given workPeriod. Choose on of the existing workPeriods");
        workPeriods.remove(workPeriod.getIdentifier());
    }

 */

    public void removeWorkPeriod(WorkPeriod workPeriod) throws IllegalArgumentException {
        if(workPeriod== null) throw new IllegalArgumentException("The employee's workPeriods doesn't contain the given workPeriod. Choose on of the existing workPeriods");
        if(!workPeriods.contains(workPeriod)) throw new IllegalArgumentException("The employee's workPeriods doesn't contain the given workPeriod. Choose on of the existing workPeriods");
        workPeriods.remove(workPeriod);
    }

    public WorkPeriod mergeTwoWorkPeriods(WorkPeriod workPeriod1, WorkPeriod workPeriod2) {
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

    /*
    @Override public Iterator<WorkPeriod> iterator() {
        return workPeriods.values().iterator();
    }*/

    @Override public Iterator<WorkPeriod> iterator() {
        return workPeriods.iterator();
    }
}


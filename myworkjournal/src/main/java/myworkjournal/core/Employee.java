package myworkjournal.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Employee implements Iterable<WorkPeriod> {

    private String name;
    private HashMap<String, WorkPeriod> workPeriods; //Collection eller array?

    public Employee(String name) {
        setName(name);
        this.workPeriods = new HashMap<>();
    }

    public Employee(String name, HashMap<String, WorkPeriod> workPeriods) {
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

    public HashMap<String, WorkPeriod> getWorkPeriods() {
        return this.workPeriods;
    }

    public void addWorkPeriod(WorkPeriod workPeriod) {
        if(workPeriods.containsKey(workPeriod.getIdentifier())) {
            workPeriods.put(workPeriod.getIdentifier(), mergeTwoWorkPeriods(workPeriods.get(workPeriod.getIdentifier()), workPeriod));
        }
        else {

        workPeriods.put(workPeriod.getIdentifier(), workPeriod);
        }
    }

    public void removeWorkPeriod(WorkPeriod workPeriod) throws IllegalArgumentException {
        if(workPeriod== null) throw new IllegalArgumentException("The employee's workPeriods doesn't contain the given workPeriod. Choose on of the existing workPeriods");
        if(!workPeriods.containsKey(workPeriod.getIdentifier())) throw new IllegalArgumentException("The employee's workPeriods doesn't contain the given workPeriod. Choose on of the existing workPeriods");
        workPeriods.remove(workPeriod.getIdentifier());
    }

    public WorkPeriod mergeTwoWorkPeriods(WorkPeriod workPeriod1, WorkPeriod workPeriod2) {
        Collection<Work> workHistory1 = workPeriod1.getPeriodWorkHistory();
        Collection<Work> workHistory2 = workPeriod2.getPeriodWorkHistory();
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

    @Override public Iterator<WorkPeriod> iterator() {
        return workPeriods.values().iterator();
    }
}


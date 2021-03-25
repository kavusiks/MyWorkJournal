package myworkjournal.core;

import java.util.Collection;
import java.util.HashMap;

public class Employee {

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
            // Legg til try catch...
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

    public WorkPeriod mergeTwoWorkPeriods(WorkPeriod workPeriod1, WorkPeriod workPeriod2) {
        WorkPeriod result = workPeriod1;
        Collection<Work> workHistory1 = workPeriod1.getWorkHistory();
        Collection<Work> workHistory2 = workPeriod2.getWorkHistory();
        for (Work work : workHistory2) {
            workHistory1.add(work);
        }
        result.setPeriodWorkHistory(workHistory1);
        return result;
    }

    public String getName(){
        return this.name;
    }

    @Override public String toString() {
        return "Employee{" + "name='" + name + '\'' + ", workPeriods=" + workPeriods + '}';
    }
}


package myworkjournal.persistence;

import myworkjournal.core.Employee;
import myworkjournal.core.Work;
import myworkjournal.core.WorkPeriod;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

public class EmployeePersistence implements DataSaverInterface<Employee> {

  private String filepath;


 // private Employee employee;

  public EmployeePersistence (String filepath) {
    this.filepath = filepath;
  }

  /*
  public EmployeePersistence (String filepath, Employee employee) {
    this(filepath);
    this.employee = employee;
  }
  */

/*
  public Employee getEmployee() {
    return employee;
  }
  */
  @Override public Employee deserialize(Scanner inFile) {
    while (inFile.hasNext()) {
      String name;
      Employee employee;
      //KANSKJE JEG BURDE TA HAS NEXT HER, MULIG SOM EN STATISK METODE INTERFACEN
      String nextLine = DataSaverInterface.nextLineIfItHas(inFile);
      //Sjekkes i metoden over
      //assert nextLine != null;
      if (nextLine.strip().equals("Employee {")) {
        nextLine = DataSaverInterface.nextLineIfItHas(inFile);
        if (nextLine.contains("name")) {
          name = nextLine.replace("name:", "").strip();
          employee = new Employee(name);
        }
        else {
          throw new IllegalStateException("Save file doesn't contain proper employee info");
        }
        nextLine = DataSaverInterface.nextLineIfItHas(inFile);
        if (nextLine.contains("workPeriods:")) {
          if (!nextLine.replace("workPeriods: [ amount= ", "").strip().equals("0")) {
            WorkPeriod workPeriodToAdd;
            WorkPeriodPersistence workPeriodPersistence = new WorkPeriodPersistence(filepath);
            while (!nextLine.strip().equals("]")) {
              workPeriodToAdd = workPeriodPersistence.deserialize(inFile);
              if (workPeriodToAdd != null) {
                employee.addWorkPeriod(workPeriodToAdd);
              }
              if (inFile.hasNext()) {
                nextLine = DataSaverInterface.nextLineIfItHas(inFile);
              }
            }
          } else {
            //Dette er for å lese av "]" dersom lista er tom
            DataSaverInterface.nextLineIfItHas(inFile);
          }
        }
          //For å lese ferdig siste "}" av objektet og nå bunnen av filen
          nextLine = DataSaverInterface.nextLineIfItHas(inFile);
        if (nextLine.contains("} /Employee")) {
          //TODO vurder behovet fo rå ha en this.worp... framfor å returnere workperiod direkte
          //this.employee = employee;
            return employee;
          } else {
            throw new IllegalStateException("Save file doesn't contain proper workPeriod info");
          }
        }
    }

    //System.out.println(employee.toString());
    //System.out.println(employee.getName());
    return null;
  }
  @Override public Employee readFile() throws FileNotFoundException {
    Scanner inFile = new Scanner((new FileReader(filepath)));
    Employee readEmployee = deserialize(inFile);
    inFile.close();
    return readEmployee;
  }


  @Override public void serialize(PrintWriter outFile, Employee employeeToSerialize) {
    outFile.println("Employee {");
    outFile.println("  name: " + employeeToSerialize.getName());
    outFile.println("  workPeriods: [ amount= " + employeeToSerialize.getWorkPeriods().size());
    boolean firstWorkPeriodSerialized= false;
    for (WorkPeriod workPeriod : employeeToSerialize) {
      if (firstWorkPeriodSerialized) {
        outFile.println("  ,");
      }
      else {
        firstWorkPeriodSerialized = true;
      }
      WorkPeriodPersistence workPeriodPersistence = new WorkPeriodPersistence(filepath);
      workPeriodPersistence.serialize(outFile, workPeriod);
    }

    outFile.println("   ]");
    outFile.println("} /Employee");
  }

  @Override public void writeFile(Employee employeeToWrite) throws FileNotFoundException {
    PrintWriter outFile = new PrintWriter(filepath);
    serialize(outFile,employeeToWrite);
    outFile.close();
  }

  public static void main(String[] args) throws FileNotFoundException {
    Work work1 = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(3));
    Work work2 = new Work(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(4));
    Work work3 = new Work(LocalDateTime.now().minusHours(10), LocalDateTime.now().minusHours(5));
    Employee e = new Employee("ole");
    Work work11 = new Work(LocalDateTime.now().minusHours(1).plusMonths(1), LocalDateTime.now().plusHours(4).plusMonths(1));
    Work work22 = new Work(LocalDateTime.now().minusDays(2).plusMonths(1), LocalDateTime.now().minusDays(2).plusHours(4).plusMonths(1));
    WorkPeriod workPeriod1 = new WorkPeriod(WorkPeriod.months.get(LocalDate.now().getMonthValue()-1), LocalDate.now()
        .getYear(), 200);
    workPeriod1.addWork(work1);
    workPeriod1.addWork(work2);
    workPeriod1.addWork(work3);
    WorkPeriod workPeriod11 = new WorkPeriod(WorkPeriod.months.get(LocalDate.now().getMonthValue()), LocalDate.now()
        .getYear(), 200);
    workPeriod11.addWork(work11);
    workPeriod11.addWork(work22);
    e.addWorkPeriod(workPeriod1);
    e.addWorkPeriod(workPeriod11);
    //EmployeePersistence ep = new EmployeePersistence("src/main/resources/myworkjournal/persistence/employee.txt");
    EmployeePersistence ep2 = new EmployeePersistence("src/main/resources/myworkjournal/persistence/employee.txt");
    ep2.writeFile(e);
    ep2.readFile();
    //System.out.println(e.getWorkPeriods().toString());
    //System.out.println("EMPLOYEE" + ep2.getEmployee());
  }
}

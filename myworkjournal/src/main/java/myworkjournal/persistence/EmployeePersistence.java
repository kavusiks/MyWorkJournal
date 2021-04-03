package myworkjournal.persistence;

import myworkjournal.core.Employee;
import myworkjournal.core.Work;
import myworkjournal.core.WorkPeriod;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

public class EmployeePersistence implements DataSaver {

  private String filepath;


  private Employee employee;

  public EmployeePersistence (String filepath) {
    this.filepath = filepath;
  }

  public EmployeePersistence (String filepath, Employee employee) {
    this(filepath);
    this.employee = employee;
  }

  public Employee getEmployee() {
    return employee;
  }
  @Override public Employee deserialize(Scanner inFile) {
    while (inFile.hasNext()) {
      String name;
      Employee employee;
      //KANSKJE JEG BURDE TA HAS NEXT HER, MULIG SOM EN STATISK METODE INTERFACEN
      String nextLine = DataSaver.nextLineIfItHas(inFile);
      //Sjekkes i metoden over
      //assert nextLine != null;
      if (nextLine.strip().equals("Employee {")) {
        nextLine = DataSaver.nextLineIfItHas(inFile);
        if (nextLine.contains("name")) {
          name = nextLine.replace("name:", "").strip();
          employee = new Employee(name);
        }
        else {
          throw new IllegalStateException("Save file doesn't contain proper employee info");
        }
        nextLine = DataSaver.nextLineIfItHas(inFile);
        if (nextLine.contains("workPeriods:")) {
          WorkPeriod workPeriodToAdd = null;
          WorkPeriodPersistence workPeriodPersistence = new WorkPeriodPersistence(filepath);
          while (!nextLine.equals("} /Employee")) {
            workPeriodToAdd = workPeriodPersistence.deserialize(inFile);
            if (workPeriodToAdd != null) {
              employee.addWorkPeriod(workPeriodToAdd);
            }
            if(inFile.hasNext()){
              nextLine = DataSaver.nextLineIfItHas(inFile);
            }
          }
        }
        this.employee = employee;
      }
    }

    //System.out.println(employee.toString());
    //System.out.println(employee.getName());
    return null;
  }
  @Override public void readFile() throws FileNotFoundException {
    Scanner inFile = new Scanner((new FileReader(filepath)));
    deserialize(inFile);
    inFile.close();
  }


  @Override public void serialize(PrintWriter outFile, int indentation) {
    outFile.println("Employee {");
    outFile.println("  name: " + employee.getName());
    outFile.println("  workPeriods: {");
    WorkPeriodPersistence workPeriodPersistence;
    for (Iterator<WorkPeriod> it = employee.iterator(); it.hasNext(); ) {
      WorkPeriod workPeriod = it.next();
      workPeriodPersistence = new WorkPeriodPersistence(filepath, workPeriod);
      workPeriodPersistence.serialize(outFile, 4);
      if(it.hasNext()){
        outFile.println(",");
      }
    }
    outFile.println("} /Employee");
  }

  @Override public void writeFile() throws FileNotFoundException {
    PrintWriter outFile = new PrintWriter(filepath);
    serialize(outFile, 0);
    outFile.close();
  }

  public static void main(String[] args) throws FileNotFoundException {
    Work work1 = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(3));
    Work work2 = new Work(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusHours(4));
    Work work3 = new Work(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(5));
    Employee e = new Employee("ole");
    Work work11 = new Work(LocalDateTime.now().minusHours(1).plusMonths(1), LocalDateTime.now().plusHours(4).plusMonths(1));
    Work work22 = new Work(LocalDateTime.now().minusDays(2).plusMonths(1), LocalDateTime.now().plusHours(4).plusMonths(1));
    WorkPeriod workPeriod1 = new WorkPeriod("mars", 2021, 200);
    workPeriod1.addWork(work1);
    workPeriod1.addWork(work2);
    workPeriod1.addWork(work3);
    WorkPeriod workPeriod11 = new WorkPeriod("april", 2021, 200);
    workPeriod11.addWork(work11);
    workPeriod11.addWork(work22);
    e.addWorkPeriod(workPeriod1);
    e.addWorkPeriod(workPeriod11);
    EmployeePersistence ep = new EmployeePersistence("src/main/resources/myworkjournal/persistence/employee.txt", e);
    EmployeePersistence ep2 = new EmployeePersistence("src/main/resources/myworkjournal/persistence/employee.txt");
    ep.writeFile();
    ep2.readFile();
    //System.out.println(e.getWorkPeriods().toString());
    //System.out.println("EMPLOYEE" + ep2.getEmployee());
  }
}

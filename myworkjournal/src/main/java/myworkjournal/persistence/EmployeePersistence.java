package myworkjournal.persistence;

import myworkjournal.core.Employee;
import myworkjournal.core.WorkPeriod;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class EmployeePersistence extends AbstractPersistence implements DataSaverInterface<Employee> {

  public EmployeePersistence(String filepath) {
    this.filepath = filepath;
  }


  /**
   * Used to read from file and then deserialize.
   *
   * @return the read object of T
   * @throws FileNotFoundException if the file is not found.
   */
  @Override public Employee readFile() throws FileNotFoundException, IllegalStateException {
    Scanner inFile = new Scanner((new FileReader(filepath)));
    Employee readEmployee = deserialize(inFile);
    inFile.close();
    return readEmployee;
  }

  /**
   * Used to serialize and the write to file.
   *
   * @throws FileNotFoundException if the file can't be written to.
   */
  @Override public void writeFile(Employee employeeToWrite) throws FileNotFoundException {
    PrintWriter outFile = new PrintWriter(filepath);
    serialize(outFile, employeeToWrite);
    outFile.close();
  }

  /**
   * Used to deserialize an object from a scanner that is reading from a file.
   * This method is used in readFile and sometimes by itself in other persistence classes.
   * This method receives data from the file by reading and collecting expected value line by line.
   * These read values are then used to recreate the saved file.
   *
   * @param inFile the scanner that can read the file.
   * @return the deserialized object of T.
   * @throws IllegalStateException if the reading file isn't properly written.
   */
  @Override public Employee deserialize(Scanner inFile) throws IllegalStateException {
    while (inFile.hasNext()) {
      String name;
      Employee employee;
      String nextLine = nextLineIfItHas(inFile);
      if (nextLine.strip().equals("Employee {")) {
        nextLine = nextLineIfItHas(inFile);
        if (nextLine.contains("name")) {
          name = nextLine.replace("name:", "").strip();
          employee = new Employee(name);
        } else {
          throw new IllegalStateException("Save file doesn't contain proper employee info");
        }
        nextLine = nextLineIfItHas(inFile);
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
                nextLine = nextLineIfItHas(inFile);
              }
            }
          } else {
            //Calling nextLine() her to reach the list's endmark("]") in the empty list
            nextLineIfItHas(inFile);
          }
        }
        //Calling nextLine() her to reach the object's endmark("}") at bottom of the file.
        nextLine = nextLineIfItHas(inFile);
        if (nextLine.contains("} /Employee")) {
          return employee;
        } else {
          throw new IllegalStateException("Save file doesn't contain proper workPeriod info");
        }
      }
    }
    return null;
  }

  /**
   * Used to serialize an object with a printWriter that is going to write to a file.
   * This method is used in writeFile and sometimes by itself in other persistence classes.
   * This method receives data from the object in the class and writes the values line by line
   * in a specific order.
   *
   * @param outFile the printWriter that will write the data to the it's file.
   */
  @Override public void serialize(PrintWriter outFile, Employee employeeToSerialize) {
    outFile.println("Employee {");
    outFile.println("  name: " + employeeToSerialize.getName());
    outFile.println("  workPeriods: [ amount= " + employeeToSerialize.getWorkPeriods().size());
    boolean firstWorkPeriodSerialized = false;
    for (WorkPeriod workPeriod : employeeToSerialize) {
      if (firstWorkPeriodSerialized)
        outFile.println("  ,");
      else
        firstWorkPeriodSerialized = true;
      WorkPeriodPersistence workPeriodPersistence = new WorkPeriodPersistence(filepath);
      workPeriodPersistence.serialize(outFile, workPeriod);
    }

    outFile.println("   ]");
    outFile.println("} /Employee");
  }
}


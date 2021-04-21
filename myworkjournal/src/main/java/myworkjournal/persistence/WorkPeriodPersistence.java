package myworkjournal.persistence;

import myworkjournal.core.Work;
import myworkjournal.core.WorkPeriod;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Peristence class for WorkPeriod. This is used by the persistence class for Employee.
 */
public class WorkPeriodPersistence extends AbstractPersistence implements DataSaverInterface<WorkPeriod> {


  public WorkPeriodPersistence(String filepath) {
    this.filepath = filepath;
  }


  /**
   * Used to read from file and then deserialize.
   * @return the read object of T
   * @throws FileNotFoundException if the file is not found.
   */
  @Override public WorkPeriod readFile() throws FileNotFoundException {
    Scanner inFile = new Scanner((new FileReader(filepath)));
    WorkPeriod readWorkPeriod = deserialize(inFile);
    inFile.close();
    return readWorkPeriod;
  }


  /**
   * Used to serialize and the write to file.
   * @throws FileNotFoundException if the file can't be written to.
   */
  @Override public void writeFile(WorkPeriod workPeriodToWrite) throws FileNotFoundException {
    PrintWriter outFile = new PrintWriter(filepath);
    serialize(outFile, workPeriodToWrite);
    outFile.close();
  }


  /**
   * Used to deserialize an object from a scanner that is reading from a file.
   * This method is used in readFile and sometimes by itself in other persistence classes.
   * This method receives data from the file by reading and collecting expected value line by line.
   * These read values are then used to recreate the saved file.
   * @param inFile the scanner that can read the file.
   * @return the deserialized object of T.
   * @throws IllegalStateException if the reading file isn't properly written.
   */
  public WorkPeriod deserialize(Scanner inFile) {

    while (inFile.hasNext()) {
      String month = "";
      WorkPeriod workPeriod;
      int year = 0;
      int hourlyWage = 0;
      String nextLine = nextLineIfItHas(inFile);
      if (nextLine.strip().equals("WorkPeriod {")) {
        nextLine = nextLineIfItHas(inFile);
        if (nextLine.contains("month"))
          month = nextLine.replace("month:", "").strip();
        nextLine = nextLineIfItHas(inFile);
        if (nextLine.contains("year"))
          year = Integer.parseInt(nextLine.replace("year:", "").strip());
        nextLine = nextLineIfItHas(inFile);
        if (nextLine.contains("hourlyWage"))
          hourlyWage = Integer.parseInt(nextLine.replace("hourlyWage:", "").strip());

        if (!month.equals("") && year != 0 && hourlyWage != 0) {
          workPeriod = new WorkPeriod(month, year, hourlyWage);
        } else {
          throw new IllegalStateException("Savefile doesn't contain proper workPeriod info");
        }
        nextLine = nextLineIfItHas(inFile);
        if (nextLine.contains("periodWorkHistory")) {
          if (!nextLine.replace("periodWorkHistory: [ amount=", "").strip().equals("0")) {
            Work workToAdd;
            WorkPersistence workPersistence = new WorkPersistence(filepath);
            while (!nextLine.strip().equals("]")) {
              workToAdd = workPersistence.deserialize(inFile);
              if (workToAdd != null)
                workPeriod.addWork(workToAdd);
              //Calling nextLine() her to reach the separator(",") or the list's endmark("]") in the list with Works
              nextLine = nextLineIfItHas(inFile);
            }
          } else {
            //Calling nextLine() her to reach the list's endmark("]") in the empty list
            nextLineIfItHas(inFile);
          }
        }
        //Calling nextLine() her to reach the object's endmark("}") at bottom of the file.
        nextLine = nextLineIfItHas(inFile);
        if (nextLine.contains("} /WorkPeriod")) return workPeriod;
        else throw new IllegalStateException("Save file doesn't contain proper workPeriod info");
      }
    }
    return null;
  }

  /**
   * Used to serialize an object with a printWriter that is going to write to a file.
   * This method is used in writeFile and sometimes by itself in other persistence classes.
   * This method receives data from the object in the class and writes the values line by line
   * in a specific order.
   * @param outFile the printWriter that will write the data to the it's file.
   */
  @Override public void serialize(PrintWriter outFile, WorkPeriod workPeriodToSerialize) {
    {
      outFile.println("WorkPeriod {");
      outFile
          .println("  month: " + WorkPeriod.months.get(workPeriodToSerialize.getPeriodStartDate().getMonthValue() - 1));
      outFile.println("  year: " + workPeriodToSerialize.getPeriodStartDate().getYear());
      outFile.println("  hourlyWage: " + workPeriodToSerialize.getHourlyWage());
      outFile.println("  periodWorkHistory: [ amount=" + workPeriodToSerialize.getPeriodWorkHistory().size());
      boolean firstWorkSerialized= false;
      for (Work work : workPeriodToSerialize) {
        if (firstWorkSerialized) {
          outFile.println("  ,");
        }
        else {
          firstWorkSerialized = true;
        }
        WorkPersistence workPersistence = new WorkPersistence(filepath);
        workPersistence.serialize(outFile, work);
      }
      outFile.println("    ]");
      outFile.println("} /WorkPeriod");

    }

  }

  public static void main(String[] args) throws FileNotFoundException {
    Work work1 = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(3));
    Work work2 = new Work(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(4));
    Work work3 = new Work(LocalDateTime.now().minusDays(1).minusHours(1), LocalDateTime.now().minusDays(1).plusHours(5));
    WorkPeriod workPeriod = new WorkPeriod(WorkPeriod.months.get(LocalDate.now().getMonthValue()+1), LocalDate.now()
        .getYear(), 200);
    workPeriod.addWork(work1);
    workPeriod.addWork(work2);
    workPeriod.addWork(work3);
    WorkPeriodPersistence wp1 =
        new WorkPeriodPersistence("src/main/resources/myworkjournal/persistence/workPeriod.txt");
    wp1.writeFile(workPeriod);
    wp1.readFile();

  }
}


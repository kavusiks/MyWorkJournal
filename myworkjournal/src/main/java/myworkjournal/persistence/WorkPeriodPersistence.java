package myworkjournal.persistence;

import myworkjournal.core.Work;
import myworkjournal.core.WorkPeriod;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

public class WorkPeriodPersistence implements DataSaver {



  private String filepath;
  private WorkPeriod workPeriod;

  public WorkPeriodPersistence(String filepath) {
    this.filepath = filepath;
  }

  public WorkPeriodPersistence(String filepath, WorkPeriod workPeriod) {
    this(filepath);
    this.workPeriod = workPeriod;
  }

  public WorkPeriod getWorkPeriod() {
    return this.workPeriod;
  }

  public WorkPeriod deserialize(Scanner inFile) throws FileNotFoundException {
    String month = "";
    int year = 0;
    int hourlyWage = 0;

    while (inFile.hasNext()) {
      if (inFile.nextLine().equals("WorkPeriod {")) {
        System.out.println("HAR FUNNET STARTPUNKT TIL WORKPERIOD");
        month = inFile.nextLine().replace("month:", "").strip();
        //int year = inFile.nextLine().replace("year:", "").strip());
        System.out.println("Måned i wp pers: " + month);
        year = Integer.parseInt(inFile.nextLine().replace("year:", "").strip());
        System.out.println("år i wp pers: " +year);
        //hourlyWage strip??
        hourlyWage = Integer.parseInt(inFile.nextLine().replace("hourlyWage:", "").strip());
        System.out.println("timeslønn i wp pers: " + hourlyWage);
        WorkPeriod workPeriod = new WorkPeriod(month, year, hourlyWage);
        if (inFile.nextLine().contains("periodWorkHistory:")) {
          System.out.println("Inne i if som sjekker at neste linje har periodWorkHistory");
          Work workToAdd = null;
          WorkPersistence workPersistence = new WorkPersistence(filepath);
          //sjekk om denne under kan fjernet pga det er sånt oppe
          String nextLine = "";
          while (!nextLine.strip().equals("]")) {
            System.out.println("Inne i løkka siden nextline ikke er ], nextlinje er: " + nextLine);
            //String nextLine = inFile.nextLine();
            //while (!nextLine.strip().equals("}"))
            //System.out.println("debug");
            //System.out.println(inFile.nextLine());
            //System.out.println(workPersistence.deserialize(inFile));
            workToAdd = workPersistence.deserialize(inFile);
            if (workToAdd != null) {
              workPeriod.addWork(workToAdd);
              //      System.out.println("HEEEER: " + workPeriod.getPeriodWorkHistory());
            }
            if (inFile.hasNext()){
              nextLine = inFile.nextLine();
            }
            //System.out.println("wp gjorde om til" + nextLine);
          }
        }
        //for å lese ferdig siste "}" av objektet
        //For å nå bunnen av filen
        if(inFile.hasNext()){
        inFile.nextLine();
        }
        this.workPeriod = workPeriod;
        return workPeriod;
      }

    }
    return null;
  }

  @Override public void readFile() throws FileNotFoundException {
    Scanner inFile = new Scanner((new FileReader(filepath)));
    deserialize(inFile);
    inFile.close();
  }



  public void serialize(PrintWriter outFile, int indentation) {
    String valueIndentationString = "";
    String objectIndentationString = "";
    for (int i = 0; i < indentation; i++) {
      valueIndentationString += " ";
    }
    for (int i = 0; i < indentation / 2; i++) {
      objectIndentationString += " ";
    }

    outFile.println("WorkPeriod {");
    outFile.println("  month: " + WorkPeriod.months.get(workPeriod.getPeriodStartDate().getMonthValue() - 1));
    outFile.println("  year: " + workPeriod.getPeriodStartDate().getYear());
    outFile.println("  hourlyWage: " + workPeriod.getHourlyWage());
    outFile.println("  periodWorkHistory: [");
    WorkPersistence workPersistence;

    for (Iterator<Work> it = workPeriod.iterator(); it.hasNext(); ) {
      Work work = it.next();
      workPersistence = new WorkPersistence(filepath, work);
      workPersistence.serialize(outFile, 4);
      if (it.hasNext()) {
        outFile.println("  ,");
      }
    }

    outFile.println("    ]");
    outFile.println("} /WorkPeriod");

  }

  @Override public void writeFile() throws FileNotFoundException {
    PrintWriter outFile = new PrintWriter(filepath);
    serialize(outFile, 4);
    outFile.close();
  }

  public static void main(String[] args) throws FileNotFoundException {
    Work work1 = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(3));
    Work work2 = new Work(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusHours(4));
    Work work3 = new Work(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(5));
    WorkPeriod workPeriod = new WorkPeriod("mars", 2021, 200);
    workPeriod.addWork(work1);
    //Fiks adde flere i serializer i workpersistence
    workPeriod.addWork(work2);
    workPeriod.addWork(work3);
    WorkPeriodPersistence wp =
        new WorkPeriodPersistence("src/main/resources/myworkjournal/persistence/workPeriod.txt", workPeriod);
    WorkPeriodPersistence wp1 =
        new WorkPeriodPersistence("src/main/resources/myworkjournal/persistence/workPeriod.txt");
    wp.writeFile();
    wp1.readFile();
    System.out.println(wp1.getWorkPeriod().getIdentifier());
    System.out.println(wp1.getWorkPeriod().getPeriodWorkHistory());

  }
}

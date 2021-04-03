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
  private WorkPeriod workPeriodToSerialize;

  public WorkPeriodPersistence(String filepath) {
    this.filepath = filepath;
  }

  public WorkPeriodPersistence(String filepath, WorkPeriod workPeriodToSerialize) {
    this(filepath);
    this.workPeriodToSerialize = workPeriodToSerialize;
  }

  public WorkPeriod getWorkPeriod() {
    return this.workPeriodToSerialize;
  }

  public WorkPeriod deserialize(Scanner inFile) {

    while (inFile.hasNext()) {
      String month = "";
      WorkPeriod workPeriod;
      int year = 0;
      int hourlyWage = 0;
      String nextLine = DataSaver.nextLineIfItHas(inFile);
      if (nextLine.strip().equals("WorkPeriod {")) {
        nextLine = DataSaver.nextLineIfItHas(inFile);
        if (nextLine.contains("month")) month = nextLine.replace("month:", "").strip();
        nextLine = DataSaver.nextLineIfItHas(inFile);
        if (nextLine.contains("year")) year = Integer.parseInt(nextLine.replace("year:", "").strip());
        nextLine = DataSaver.nextLineIfItHas(inFile);
        if (nextLine.contains("hourlyWage")) hourlyWage = Integer.parseInt(nextLine.replace("hourlyWage:", "").strip());

        if(!month.equals("") && year != 0 && hourlyWage != 0) {
          workPeriod = new WorkPeriod(month, year, hourlyWage);
        }
        else {
          throw new IllegalStateException("Savefile doesn't contain proper workPeriod info");
        }
        nextLine = DataSaver.nextLineIfItHas(inFile);
        if (nextLine.contains("periodWorkHistory") && !nextLine.replace("periodWorkHistory: [ amount=", "").strip().equals("0")) {
          Work workToAdd;
          WorkPersistence workPersistence = new WorkPersistence(filepath);
          //nextLine = DataSaver.nextLineIfItHas(inFile);
          while (!nextLine.strip().equals("]")) {
            //System.out.println("Inne i løkka siden nextline ikke er ], nextlinje er: " + nextLine);
            workToAdd = workPersistence.deserialize(inFile);
            if (workToAdd != null) workPeriod.addWork(workToAdd);
            //Brukes for å nå "," eller "]"
            nextLine = DataSaver.nextLineIfItHas(inFile);
          }
        }
        else {
          //Dette er for å lese av "]" dersom lista er tom
          DataSaver.nextLineIfItHas(inFile);
        }
        //For å lese ferdig siste "}" av objektet og nå bunnen av filen
        nextLine = DataSaver.nextLineIfItHas(inFile);
        if (nextLine.contains("} /WorkPeriod")) {
          //TODO vurder behovet fo rå ha en this.worp... framfor å returnere workperiod direkte
          this.workPeriodToSerialize = workPeriod;
          return workPeriodToSerialize;
        }
        else {
          throw new IllegalStateException("Save file doesn't contain proper workPeriod info");
        }
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
    outFile.println("  month: " + WorkPeriod.months.get(workPeriodToSerialize.getPeriodStartDate().getMonthValue() - 1));
    outFile.println("  year: " + workPeriodToSerialize.getPeriodStartDate().getYear());
    outFile.println("  hourlyWage: " + workPeriodToSerialize.getHourlyWage());
    outFile.println("  periodWorkHistory: [ amount=" + workPeriodToSerialize.getPeriodWorkHistory().size());
    WorkPersistence workPersistence;

    for (Iterator<Work> it = workPeriodToSerialize.iterator(); it.hasNext(); ) {
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
    //System.out.println(wp1.getWorkPeriod().getIdentifier());
    //System.out.println(wp1.getWorkPeriod().getPeriodWorkHistory());

  }
}

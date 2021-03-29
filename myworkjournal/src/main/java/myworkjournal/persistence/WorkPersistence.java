package myworkjournal.persistence;

import myworkjournal.core.Work;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

public class WorkPersistence implements DataSaver {


  private String filepath;
  private Work work;

  public WorkPersistence (String filepath) {
    this.filepath = filepath;
  }

  public WorkPersistence (String filepath, Work work) {
    this(filepath);
    this.work = work;
  }

  public Work getWork(){
    //System.out.println("returnerer work med timer" + work.getHours());
    return this.work;
  }

  public Work deserialize(Scanner inFile) {
/*
    while (inFile.hasNext()){

      System.out.println(inFile.nextLine());
    }

 */
    this.work = null;
    LocalDateTime startTime = null;
    LocalDateTime endTime = null;
    //int hours = 0;
    while (inFile.hasNext()) {
      String nextLine = inFile.nextLine();
      if(nextLine.strip().equals("Work {")) {
          startTime = LocalDateTime.parse(inFile.nextLine().replace("startTime:", "").strip());
          endTime = LocalDateTime.parse(inFile.nextLine().replace("endTime:", "").strip());
        }
      if (nextLine.strip().equals("} /Work")){
        if(startTime != null && endTime != null) {
          Work work = new Work(startTime, endTime);
          this.work = work;
          //System.out.println("serialize utført med retur av work");
          //System.out.println("work" + "starttif: " + work.getStartTime() + "slutttid: " + work.getEndTime());
          return work;
        }
      }
    }
    //System.out.println("serialize utført uten retur av work");
    //System.out.println(work.getEndTime());
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
      for (int i = 0; i< indentation; i++){
        valueIndentationString+=" ";
      }
      for (int i = 0; i< indentation/2; i++){
        objectIndentationString+=" ";
      }
      outFile.println(objectIndentationString+"Work {");
      outFile.println(valueIndentationString + "startTime: " + work.getStartTime());
      outFile.println(valueIndentationString + "  endTime: " + work.getEndTime());
      //outFile.println("    hours: " + work.getHours());
      outFile.println(objectIndentationString + "} /Work");

    }

  @Override public void writeFile() throws FileNotFoundException {
    PrintWriter outFile = new PrintWriter(filepath);
    serialize(outFile, 8);
    outFile.close();
  }

  public static void main(String[] args) throws FileNotFoundException {
    Work work = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(3));
    WorkPersistence wp = new WorkPersistence("src/main/resources/myworkjournal/persistence/work.txt", work);
    WorkPersistence wp1 = new WorkPersistence("src/main/resources/myworkjournal/persistence/work.txt");
    wp.writeFile();
    wp1.readFile();
    System.out.println(wp1.getWork());

  }
}

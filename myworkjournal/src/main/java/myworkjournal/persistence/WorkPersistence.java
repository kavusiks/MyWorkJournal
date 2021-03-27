package myworkjournal.persistence;

import myworkjournal.core.Work;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

public class WorkPersistence implements DataSaver {


  String filepath;
  Work work;

  public WorkPersistence (String filepath) {
    this.filepath = filepath;
  }

  public WorkPersistence (String filepath, Work work) {
    this(filepath);
    this.work = work;
  }

  public Work getWork(){
    System.out.println("returnerer work" + work.getHours());
    return this.work;
  }



  @Override public void readFile() throws FileNotFoundException {
    Scanner inFile = new Scanner((new FileReader(filepath)));
    LocalDateTime startTime = null;
    LocalDateTime endTime = null;
    //int hours = 0;
    while (inFile.hasNext()) {
      if(inFile.nextLine().equals("{")) {
        startTime = LocalDateTime.parse(inFile.nextLine().replace("startTime:", "").strip());
        endTime = LocalDateTime.parse(inFile.nextLine().replace("endTime:", "").strip());
      }
    }
    if(startTime != null && endTime != null) {
      Work work = new Work(startTime, endTime);
      this.work = work;
      getWork();
    }

    }




  @Override public void writeFile() throws FileNotFoundException {
    PrintWriter outFile = new PrintWriter(filepath);
    outFile.println("{");
    outFile.println("   startTime: " + work.getStartTime());
    outFile.println("   endTime: " + work.getEndTime());
    //outFile.println("   hours: " + work.getHours());
    outFile.println("}");
    outFile.close();

  }

  public static void main(String[] args) throws FileNotFoundException {
    Work work = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(3));
    WorkPersistence wp = new WorkPersistence("src/main/resources/myworkjournal/persistence/work.txt", work);
    wp.writeFile();
    wp.readFile();

  }
}

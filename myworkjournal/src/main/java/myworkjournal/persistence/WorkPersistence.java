package myworkjournal.persistence;

import myworkjournal.core.Work;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

public class WorkPersistence implements DataSaverInterface<Work> {


  private String filepath;
  private Work workToSerialize;

  public WorkPersistence (String filepath) {
    this.filepath = filepath;
  }

  public WorkPersistence (String filepath, Work workToSerialize) {
    this(filepath);
    this.workToSerialize = workToSerialize;
  }

  //TODO: metode brukt for debug, fjerne tilslutt?
  public Work getWork(){
    return this.workToSerialize;
  }
  @Override
  public Work deserialize(Scanner inFile) {
    while (inFile.hasNext()) {
      LocalDateTime startTime = null;
      LocalDateTime endTime = null;
      boolean properFile = false;
      boolean workLineDetected = false;
      String nextLine = DataSaverInterface.nextLineIfItHas(inFile);
      if(nextLine.strip().equals("Work {")) {
        workLineDetected = true;
        nextLine = DataSaverInterface.nextLineIfItHas(inFile);
        if (nextLine.contains("startTime")) startTime = LocalDateTime.parse(nextLine.replace("startTime:", "").strip());
        nextLine = DataSaverInterface.nextLineIfItHas(inFile);
        if (nextLine.contains("endTime")) endTime = LocalDateTime.parse(nextLine.replace("endTime:", "").strip());
        nextLine = DataSaverInterface.nextLineIfItHas(inFile);
        if(startTime!=null && endTime!= null && nextLine.contains("} /Work")) properFile=true;
      }
      if(properFile) {
        System.out.println(startTime.toString() + endTime.toString());
        //TODO: vurder å fjerne dette
        this.workToSerialize = new Work(startTime, endTime);
        return workToSerialize;
        }
      else if (workLineDetected) {
        //hvis den når hit så er properFile = false;
        throw new IllegalStateException("Save file doesn't contain proper work info");
      }
    }
    return null;
  }


  @Override public void readFile() throws FileNotFoundException {
    Scanner inFile = new Scanner((new FileReader(filepath)));
    deserialize(inFile);
    inFile.close();
    }

    @Override
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
      outFile.println(valueIndentationString + "startTime: " + workToSerialize.getStartTime());
      outFile.println(valueIndentationString + "  endTime: " + workToSerialize.getEndTime());
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
    //System.out.println(wp1.getWork());

  }
}

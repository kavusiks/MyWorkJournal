package myworkjournal.persistence;

import myworkjournal.core.Work;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Persistence class for Work. This is used by the persistence class for WorkPeriod.
 */
public class WorkPersistence extends AbstractPersistence implements DataSaverInterface<Work> {


  public WorkPersistence(String filepath) {
    this.filepath = filepath;
  }

  public static void main(String[] args) throws FileNotFoundException {
    Work work = new Work(LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(3));
    //WorkPersistence wp = new WorkPersistence("src/main/resources/myworkjournal/persistence/work.txt", work);
    WorkPersistence wp1 = new WorkPersistence("src/main/resources/myworkjournal/persistence/work.txt");
    wp1.writeFile(work);
    wp1.readFile();
    //System.out.println(wp1.getWork());

  }

  /**
   * Used to read from file and then deserialize.
   *
   * @return the read object of T
   * @throws FileNotFoundException if the file is not found.
   */
  @Override public Work readFile() throws FileNotFoundException, IllegalStateException {
    Scanner inFile = new Scanner((new FileReader(filepath)));
    Work readWork = deserialize(inFile);
    inFile.close();
    return readWork;
  }

  /**
   * Used to serialize and the write to file.
   *
   * @throws FileNotFoundException if the file can't be written to.
   */
  @Override public void writeFile(Work workToWrite) throws FileNotFoundException {
    PrintWriter outFile = new PrintWriter(filepath);
    serialize(outFile, workToWrite);
    outFile.close();
  }

  /**
   * Used to deserialize an object from a scanner that is reading from a file.
   * This method is used in readFile and sometimes byt itself in other persistence classes.
   * This method receives data from the file by reading and collecting expected value line by line.
   * These read values are then used to recreate the saved file.
   *
   * @param inFile the scanner that can read the file.
   * @return the deserialized object of T.
   * @throws IllegalStateException if the reading file isn't properly written.
   */
  @Override public Work deserialize(Scanner inFile) throws IllegalStateException {
    if (inFile.hasNext()) {
      LocalDateTime startTime = null;
      LocalDateTime endTime = null;
      boolean properFile = false;
      String nextLine = nextLineIfItHas(inFile);
      if (nextLine.strip().equals("Work {")) {
        nextLine = nextLineIfItHas(inFile);
        if (nextLine.contains("startTime"))
          startTime = LocalDateTime.parse(nextLine.replace("startTime:", "").strip());
        nextLine = nextLineIfItHas(inFile);
        if (nextLine.contains("endTime"))
          endTime = LocalDateTime.parse(nextLine.replace("endTime:", "").strip());
        nextLine = nextLineIfItHas(inFile);
        if (startTime != null && endTime != null && nextLine.contains("} /Work"))
          properFile = true;
      }
      if (properFile)
        return new Work(startTime, endTime);
      else
        throw new IllegalStateException("Save file doesn't contain proper work info");
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
  @Override public void serialize(PrintWriter outFile, Work workToSerialize) {
    outFile.println(objectFieldIndentationString + "Work {");
    outFile.println(valueFieldIndentationString + "startTime: " + workToSerialize.getStartTime());
    outFile.println(valueFieldIndentationString + "  endTime: " + workToSerialize.getEndTime());
    outFile.println(objectFieldIndentationString + "} /Work");

  }
}


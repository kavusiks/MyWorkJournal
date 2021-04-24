package myworkjournal.persistence;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;



/**
 * Interface used by all of the persistence classes.
 *
 * @param <T> The Object that the persistence class is for.
 */
public interface DataSaverInterface<T> {


  /**
   * Used to read from file and then deserialize.
   *
   * @return the read object of T
   * @throws FileNotFoundException if the file is not found.
   */
  T readFile() throws FileNotFoundException, IllegalStateException;

  /**
   * Used to serialize and the write to file.
   *
   * @throws FileNotFoundException if the file can't be written to.
   */
  void writeFile(T objectToWrite) throws FileNotFoundException;

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
  T deserialize(Scanner inFile) throws IllegalStateException;

  /**
   * Used to serialize an object with a printWriter that is going to write to a file.
   * This method is used in writeFile and sometimes by itself in other persistence classes.
   * This method receives data from the object in the class and writes the values line by line
   * in a specific order.
   *
   * @param outFile the printWriter that will write the data to the it's file.
   */
  void serialize(PrintWriter outFile, T objectToSerialize);

}

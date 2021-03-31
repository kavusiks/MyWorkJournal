package myworkjournal.persistence;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public interface DataSaver<T> {
  void readFile() throws FileNotFoundException;
  void writeFile() throws FileNotFoundException;
  T deserialize(Scanner inFile);
  void serialize(PrintWriter outFile, int indentation);
  static String nextLineIfItHas(Scanner inFile) {
    if (inFile.hasNext()) return inFile.nextLine();
    return null;
  }
}

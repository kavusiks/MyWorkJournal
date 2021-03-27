package myworkjournal.persistence;

import java.io.FileNotFoundException;

public interface DataSaver {
  void readFile() throws FileNotFoundException;
  void writeFile() throws FileNotFoundException;
}

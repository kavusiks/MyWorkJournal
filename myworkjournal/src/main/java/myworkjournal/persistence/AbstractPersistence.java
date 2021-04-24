package myworkjournal.persistence;

import java.util.Scanner;

public class AbstractPersistence {

  protected String filepath;

  //Used as indentation when writing the textfile with save to make it more readable
  protected String valueFieldIndentationString = "    ";
  protected String objectFieldIndentationString = "  ";


  /**
   * Static method used to verify that the scanner has next(), before calling next to avoid error.
   *
   * @param inFile the scanner we are reading
   * @return next line if it has, else null.
   */
  protected String nextLineIfItHas(Scanner inFile) {
    if (inFile.hasNext())
      return inFile.nextLine();
    return null;
  }
}

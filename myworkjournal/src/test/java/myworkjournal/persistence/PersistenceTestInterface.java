package myworkjournal.persistence;



import java.io.IOException;

public interface PersistenceTestInterface {


  /**
   * Method used to delete the savefile created while testing persistence.
   */
  void cleanUp() throws IOException;

  void setUp();

  void testConstructor();

  /**
   * Common method used to test reading and writing from/to the savefile in the persistence class.
   * serialize() and deserialize() are tested within the readFile() and writeFile()
   */
  void testWriteAndReadFile();



}

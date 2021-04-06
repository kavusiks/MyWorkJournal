package myworkjournal.persistence;




public interface PersistenceTestInterface {

  void setUp();

  void testConstructor();

  /**
   * Common method used to test reading and writing from/to the savefile in the persistence class.
   * serialize() and deserialize() are tested within the readFile() and writeFile()
   *
   */
  void testWriteAndReadFile();



}

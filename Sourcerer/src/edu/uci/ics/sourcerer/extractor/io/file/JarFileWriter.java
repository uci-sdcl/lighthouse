package edu.uci.ics.sourcerer.extractor.io.file;

import edu.uci.ics.sourcerer.extractor.io.IJarFileWriter;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class JarFileWriter extends ExtractorWriter implements IJarFileWriter {
  public JarFileWriter(PropertyManager properties) {
    super(properties, Property.JAR_FILE_FILE);
  }
  
  public void writeJarFile(String filename) {
    write(filename);
  }
}

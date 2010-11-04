package edu.uci.ics.sourcerer.extractor.io.file;

import edu.uci.ics.sourcerer.extractor.io.IFileWriter;
import edu.uci.ics.sourcerer.model.FileEX;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class FileWriter extends ExtractorWriter implements IFileWriter {
  public FileWriter(PropertyManager properties) {
    super(properties, Property.FILE_FILE);
  }

  public void writeFile(String filename, int errorCount) {
    write(FileEX.getLine(convertToRelativePath(filename), errorCount));
  }
}
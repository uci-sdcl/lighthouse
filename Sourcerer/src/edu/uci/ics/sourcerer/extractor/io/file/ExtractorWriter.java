package edu.uci.ics.sourcerer.extractor.io.file;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import edu.uci.ics.sourcerer.extractor.io.IExtractorWriter;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public abstract class ExtractorWriter implements IExtractorWriter {
  private BufferedWriter writer;
  private String repo;
//  private String[] rootPaths;
  
  public ExtractorWriter(PropertyManager properties, Property property) {
//    rootPaths = properties.getValue(Property.INPUT).split(";");
//    for (int i = 0; i < rootPaths.length; i++) {
//      rootPaths[i] = new File(rootPaths[i]).getPath().replace('\\', '/');
//    }
    if (properties.isSet(Property.REPO_MODE)) {
      repo = properties.getValue(Property.REPO_ROOT);
    }
    try {
      writer = new BufferedWriter(new FileWriter(new File(properties.getValue(Property.OUTPUT), properties.getValue(property))));
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error opening file", e);
    }
  }
  
  protected String convertToRelativePath(String filename) {
    File file = new File(filename);
    filename = file.getPath().replace('\\', '/');
    if (repo == null) {
      return filename.replace(' ', '*');
    } else {
      if (filename.startsWith(repo)) {
        return filename.substring(repo.length()).replace(' ', '*');
      }
      logger.severe("Unable to convert " + filename + " to relative path");
      return filename;
    }
  }
  
  public void close() {
    try {
      writer.close();
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error closing writer", e);
    }
  }
  
  protected void write(String line) {
    try {
      writer.write(line);
      writer.write("\n");
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error writing line to file", e);
    }
  }
}

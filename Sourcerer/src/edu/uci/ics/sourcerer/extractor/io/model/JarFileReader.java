package edu.uci.ics.sourcerer.extractor.io.model;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;

import edu.uci.ics.sourcerer.util.Helper;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class JarFileReader {
  public static Collection<String> getJarFiles(PropertyManager properties) {
    Collection<String> jarFiles = Helper.newLinkedList();
    try {
      BufferedReader br = new BufferedReader(new FileReader(new File(properties.getValue(Property.INPUT), properties.getValue(Property.JAR_FILE_FILE))));
      for (String line = br.readLine(); line != null; line = br.readLine()) {
        jarFiles.add(line);
      }
      return jarFiles;
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to read jar file list", e);
      jarFiles.clear();
      return jarFiles;
    }
  }
}

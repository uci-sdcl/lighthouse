package edu.uci.ics.sourcerer.model;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.util.logging.Level;

public class ImportEX {
  public static String getLine(String file, String imported, boolean isStatic, boolean onDemand) {
    return file + " " + imported + " " + (isStatic ? "STATIC" : "NULL") + " " + (onDemand ? "ON_DEMAND" : "NULL");
  }
  
  public static ImportEX getImport(String line) {
    String[] parts = line.split(" ");
    if (parts.length == 5) {
      return new ImportEX(parts[0], parts[2], parts[3].equals("STATIC"), parts[4].equals("ON_DEMAND"));
    } else {
      logger.log(Level.SEVERE, "Unable to parse import: " + line);
      return null;
    }
  }

  private String file;
  private String imported;
  private boolean isStatic;
  private boolean onDemand;

  private ImportEX(String file, String imported, boolean isStatic, boolean onDemand) {
    this.file = file;
    this.imported = imported;
    this.isStatic = isStatic;
    this.onDemand = onDemand;
  }

  public String getFile() {
    return file;
  }
  
  public String getImported() {
    return imported;
  }

  public boolean isStatic() {
    return isStatic;
  }
  
  public boolean isOnDemand() {
    return onDemand;
  }
}

package edu.uci.ics.sourcerer.model;

public class FileEX {
  public static String getLine(String path, int errorCount) {
    return path + " " + errorCount;
  }
  
  public static FileEX getFile(String line) {
    String[] parts = line.split(" ");
    if (parts.length == 2) {
      return new FileEX(parts[0], parts[1]);
    } else {
      return null;
    }
  }
  
  private String path;
  private String errorCount;
  
  private FileEX(String path, String errorCount) {
    this.path = path;
    this.errorCount = errorCount;
  }
  
  public String getName() {
    return path.substring(path.lastIndexOf('/') + 1);
  }
  
  public String getPath() {
    return path;
  }
  
  public String getErrorCount() {
    return errorCount;
  }
}

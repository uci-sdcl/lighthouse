package edu.uci.ics.sourcerer.model;

public class File {
  private String fileID;
  private String name;
  private String errorCount;
  
  public File(String fileID, String name, String errorCount) {
    this.fileID = fileID;
    this.name = name;
    this.errorCount = errorCount;
  }

  public String getFileID() {
    return fileID;
  }

  public String getName() {
    return name;
  }
  
  public String getErrorCount() {
    return errorCount;
  }
}

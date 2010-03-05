package edu.uci.ics.sourcerer.model;

public class ImportDB {
  private String fileID;
  private boolean isStatic;
  private boolean onDemand;
  private TypedEntityID target;
  
  public ImportDB(String fileID, boolean isStatic, boolean dotall, TypedEntityID target) {
    this.fileID = fileID;
    this.isStatic = isStatic;
    this.onDemand = dotall;
    this.target = target;
  }

  public String getFileID() {
    return fileID;
  }

  public boolean isStatic() {
    return isStatic;
  }

  public boolean isOnDemand() {
    return onDemand;
  }

  public TypedEntityID getTarget() {
    return target;
  }
}

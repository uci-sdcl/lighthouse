package edu.uci.ics.sourcerer.repo;

import java.io.File;
import java.util.Collection;

import edu.uci.ics.sourcerer.util.Helper;

public class RepoProject {
  private Collection<File> paths;
  private String batch;
  private String checkout;
  
  public RepoProject(String batch, String checkout) {
    paths = Helper.newLinkedList();
    this.batch = batch;
    this.checkout = checkout;
  }
  
//  public RepoProject(File path, String batch, String checkout) {
//    this(batch, checkout);
//    paths.add(path);
//  }
  
  public void addPath(File path) {
    paths.add(path);
  }
  
  public Collection<File> getPaths() {
    return paths;
  }
  
  public String getInputPath() {
    StringBuffer inputPath = new StringBuffer();
    for (File path : paths) {
      if (inputPath.length() > 0) {
        inputPath.append(';');
      }
      inputPath.append(path.getPath());
    }
    return inputPath.toString();
  }
  
  public String getOutputPath(String baseOutput) {
    return baseOutput + File.separatorChar + getProject();
  }
  
  public String getProject() {
    return batch + File.separatorChar + checkout;
  }
}

package edu.uci.ics.sourcerer.repo;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

public class RepoJavaFile {
  private File file;
  private String pkg;
  private boolean invalid = false;
  
  public RepoJavaFile(File file) {
    this.file = file;
    
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.startsWith("#warn This file is preprocessed before being compiled")) {
          pkg = null;
          invalid = true;
          break;
        } else if (line.startsWith("package")) {
          int semi = line.indexOf(';');
          while (semi == -1) {
            line += br.readLine().trim();
            semi = line.indexOf(';');
          }
          pkg = line.substring(8, line.indexOf(';')).trim();
          break;
        }
      }
      br.close();
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to extract package for file!", e);
    }
  }
  
  public File getFile() {
    return file;
  }
  
  public String getPath() {
    return file.getPath();
  }
  
  public String getPackage() {
    return pkg;
  }
  
  public String getName() {
    return file.getName();
  }
  
  public String getKey() {
    return pkg + file.getName();
  }
  
  public boolean invalid() {
    return invalid;
  }
}

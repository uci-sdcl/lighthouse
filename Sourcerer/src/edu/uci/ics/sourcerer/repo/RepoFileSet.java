/*
* Sourcerer: an infrastructure for large-scale source code analysis.
* Copyright (C) by contributors. See CONTRIBUTORS.txt for full list.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uci.ics.sourcerer.repo;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.io.File;
import java.util.Collection;
import java.util.Deque;
import java.util.Map;
import java.util.logging.Level;

import edu.uci.ics.sourcerer.util.Helper;

public class RepoFileSet {
  private Map<File, RepoDir> repoMap;
  
  private Collection<File> jarFiles;
  private Map<String, Collection<RepoJavaFile>> javaFiles;
  
  public RepoFileSet(Collection<File> roots) {
    repoMap = Helper.newHashMap();
    for (File root : roots) {
      repoMap.put(root, new RepoDir());
    }
    
    jarFiles = Helper.newLinkedList();
    javaFiles = Helper.newHashMap();
  }
  
  public void addJarFile(File file) {
    jarFiles.add(file);
  }
  
  public void addJavaFile(File file) {
    File dir = file.getParentFile();
    RepoDir repoDir = repoMap.get(dir);
    if (repoDir == null) {
      Deque<File> dirStack = Helper.newStack();
      while (true) {
        dirStack.push(dir);
        dir = dir.getParentFile();
        if (dir == null) {
          logger.log(Level.SEVERE, "File not in proper directory structure! " + file.getPath());
          return;
        }
        repoDir = repoMap.get(dir);
        if (repoDir != null) {
          break;
        }
      }
      
      while (!dirStack.isEmpty()) {
        dir = dirStack.pop();
        repoDir = new RepoDir(repoDir);
        repoMap.put(dir, repoDir);
      }
    }
    repoDir.increment();
    
    RepoJavaFile javaFile = new RepoJavaFile(file);
    Collection<RepoJavaFile> files = javaFiles.get(javaFile.getKey());
    if (files == null) {
      files = Helper.newLinkedList();
      javaFiles.put(javaFile.getKey(), files);
    }
    files.add(javaFile);
  }
  
  public Collection<File> getJarFiles() {
    return jarFiles;
  }
  
  public Collection<RepoJavaFile> getUniqueJavaFiles() {
    Collection<RepoJavaFile> uniqueFiles = Helper.newLinkedList();
    for (Collection<RepoJavaFile> files : javaFiles.values()) {
      if (files.size() == 1) {
        uniqueFiles.add(files.iterator().next());
      }
    }
    return uniqueFiles;
  }
  
  public Collection<RepoJavaFile> getBestDuplicateJavaFiles() {
    Collection<RepoJavaFile> bestDuplicateFiles = Helper.newLinkedList();
    for (Collection<RepoJavaFile> files : javaFiles.values()) {
      if (files.size() > 1) {
        RepoJavaFile best = null;
        int bestValue = 0;
        for (RepoJavaFile file : files) {
          int value = getValue(file.getFile().getParentFile());
          if (value > bestValue) {
            best = file;
            bestValue = value;
          }
        }
        if (best != null) {
          bestDuplicateFiles.add(best);
        } else {
          logger.log(Level.SEVERE, "There should always be a best!");
        }
      }
    }
    return bestDuplicateFiles;
  }
  
  private int getValue(File dir) {
    RepoDir repoDir = repoMap.get(dir);
    int count = 0;
    while (repoDir != null) {
      count += repoDir.getCount();
      repoDir = repoDir.getParent();
    }
    return count;
  }
}

class RepoDir {
  private int count;
  private RepoDir parent;
  
  public RepoDir() {
    count = 0;
  }
  
  public RepoDir(RepoDir parent) {
    count = 0;
    this.parent = parent;
  }
  
  public void increment() {
    count++;
    if (parent != null) {
      parent.increment();
    }
  }
  
  public int getCount() {
    return count;
  }
  
  public RepoDir getParent() {
    return parent;
  }
}

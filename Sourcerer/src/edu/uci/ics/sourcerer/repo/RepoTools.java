package edu.uci.ics.sourcerer.repo;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Deque;
import java.util.Map;
import java.util.logging.Level;

import edu.uci.ics.sourcerer.util.Helper;
import edu.uci.ics.sourcerer.util.io.Logging;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public final class RepoTools {
  private RepoTools() {}
  
  private static File getJarIndexFolder(String repo) {
    return new File(repo, "jars");
  }
  
  private static File getLibraryIndexFolder(String repo) {
    return new File(repo, "libs");
  }
  
  public static String getLibraryOutputPath(String basePath, File libFile) {
    return basePath + File.separatorChar + "libs" + File.separatorChar + libFile.getName();
  }
  
  public static String getJarOutputPath(String basePath, IndexedJar jar) {
    return basePath + File.separatorChar + "jars" + File.separatorChar + jar.getName();
  }
  
  public static String getUnionName(String name) {
    name = name.substring(0, name.lastIndexOf('.'));
    StringBuilder builder = new StringBuilder();
    for (char c : name.toCharArray()) {
      if (Character.isLetter(c)) {
        builder.append(c);
      }
    }
    return builder.toString();
  }
  
  public static void calculateJarNameSimilarity(String repo) {
    logger.info("--- Calculating name similarity for: " + repo + " ---");

    // Create the index
    Map<RepoJar, IndexedJar> newIndex = Helper.newHashMap();
    
    // Look for jars in every project
    logger.info("Getting project listing...");
    Collection<RepoProject> projects = getProjects(repo);
    logger.info("Look at jars from " + projects.size() + " projects...");

    int projectCount = 0;
    int totalFiles = 0;

    for (RepoProject project : projects) {
      projectCount++;
      for (File path : project.getPaths()) {
        Collection<File> jarFiles = getJarFiles(path);
        logger.info("Examining " + jarFiles.size() + " jar files from project " + projectCount + " of " + projects.size());
        for (File jar : jarFiles) {
          RepoJar newJar = new RepoJar(jar);
          IndexedJar indexedJar = newIndex.get(newJar);
          if (indexedJar == null) {
            indexedJar = new IndexedJar(null);
            newIndex.put(newJar, indexedJar);
          }
          totalFiles++;
          indexedJar.addName(jar.getName());
        }
      }
    }
    
    logger.info(totalFiles + " jars found");
    
    // Write out the md5 index
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(new File(repo, "info.txt")));
      bw.write("Name Number_Matching_Name Total_Number");
      for (IndexedJar jar : newIndex.values()) {
        bw.write(jar.getNamePopularityInfo() + "\n");
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to write the md5 index", e);
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException e){}
      }
    }
    
    logger.info("--- Done! ---");
  }
  
  public static void buildJarIndex(String repo) {
    logger.info("--- Building jar index for: " + repo + " ---");
    
    // Create the jar folder
    File jarFolder = getJarIndexFolder(repo);
    if (!jarFolder.exists()) {
      jarFolder.mkdir();
    }
    
    // Load the old jar index
    Map<String, IndexedJar> oldIndex = getJarIndex(repo);
    
    // Create the new index
    Map<RepoJar, IndexedJar> newIndex = Helper.newHashMap();
        
    // Look for jars in every project
    logger.info("Getting project listing...");
    Collection<RepoProject> projects = getProjects(repo);
    logger.info("Extracting jars from " + projects.size() + " projects...");

    int projectCount = 0;
    int totalFiles = 0;
    int uniqueFiles = 0;

    for (RepoProject project : projects) {
      projectCount++;
      for (File path : project.getPaths()) {
        Collection<File> jarFiles = getJarFiles(path);
        logger.info("Extracting " + jarFiles.size() + " jar files from project " + projectCount + " of " + projects.size());
        for (File jar : jarFiles) {
          RepoJar newJar = new RepoJar(jar);
          if (!oldIndex.containsKey(newJar.getHash())) {
            IndexedJar indexedJar = newIndex.get(newJar);
            if (indexedJar == null) {
              File indexedJarFile = new File(jarFolder, uniqueFiles++ + ".jar");
              copyFile(jar, indexedJarFile);
              indexedJar = new IndexedJar(indexedJarFile);
              newIndex.put(newJar, indexedJar);
            }
            totalFiles++;
            indexedJar.addName(jar.getName());
          }
        }
      }
    }
    
    logger.info(totalFiles + " jars found");
    
    // Rename the jars
    logger.info("Renaming the " + uniqueFiles + " unique jar files");
    for (IndexedJar indexedJar : newIndex.values()) {
      indexedJar.rename();
    }
    
    // Write out the md5 index
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(new File(jarFolder, "table.txt")));
      for (Map.Entry<String, IndexedJar> jar : oldIndex.entrySet()) {
        bw.write(jar.getKey() + " " + jar.getValue().getPath() + " " + jar.getValue().getTotalCount() + "\n");
      }
      for (Map.Entry<RepoJar, IndexedJar> jar : newIndex.entrySet()) {
        bw.write(jar.getKey().getHash() + " " + jar.getValue().getPath() + " " + jar.getValue().getTotalCount() + "\n");
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to write the md5 index", e);
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException e){}
      }
    }
    
    logger.info("--- Done! ---");
  }
  
  private static void copyFile(File source, File destination) {
    FileInputStream in = null;
    FileOutputStream out = null;
    try {
      in = new FileInputStream(source);
      out = new FileOutputStream(destination);
      byte[] buffer = new byte[4096];
      int read;
      while ((read = in.read(buffer)) != -1) {
        out.write(buffer, 0, read);
      }
    } catch (IOException e) {
       logger.log(Level.SEVERE, "Unable to copy file from " + source.getPath() + " to " + destination.getPath());
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {}
      }
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {}
      }
    }
  }

  public static Map<String, IndexedJar> getJarIndex(String repo) {
    Map<String, IndexedJar> jarIndex = Helper.newHashMap();
    
    File jarIndexFile = new File(getJarIndexFolder(repo), "table.txt");
    if (jarIndexFile.exists()) {
      try {
        BufferedReader br = new BufferedReader(new FileReader(jarIndexFile));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
          String[] parts = line.split(" ");
          jarIndex.put(parts[0], new IndexedJar(parts[1], parts[2]));
        }
        return jarIndex;
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Error in reading jar md5 index");
        jarIndex.clear();
        return jarIndex;
      }
      
    } else {
      logger.log(Level.SEVERE, "No jar index file");
      return jarIndex;
    }
//    Collection<File> jarFiles = Helper.newLinkedList();
//    
//    File jarFolder = getJarIndexFolder(repo);
//    for (File jar : jarFolder.listFiles()) {
//      if (jar.isFile() && jar.getName().endsWith(".jar")) {
//        jarFiles.add(jar);
//      }
//    }
//    
//    return jarFiles;
  }
  
  public static Collection<File> getLibraryJars(String repo) {
    Collection<File> libraryJars = Helper.newLinkedList();
    
    File repoDir = getLibraryIndexFolder(repo);
    if (repoDir.exists() && repoDir.isDirectory()) {
      for (File file : repoDir.listFiles()) {
        if (file.isDirectory() && file.getName().endsWith(".jar")) {
          libraryJars.add(file);
        }
      }
    }
    
    return libraryJars;
  }
  
  public static File getExtractedJar(String repo, String jarName) {
    return new File(getJarIndexFolder(repo), jarName);
  }
  
  public static File findJarFile(String repo, String name) {
    File jar = new File(repo + File.separatorChar + "jars" + File.separatorChar + name);
    if (jar.exists()) {
      return jar;
    } else {
      return null;
    }
  }
  
  public static File findFile(String repo, String relativePath) {
    relativePath = relativePath.replace('*', ' ');
//    File checkout = new File(repo + File.separatorChar + path);
//    if (checkout.exists()) {
//      File contents = new File(checkout, "content");
//      if (contents.exists()) {
//        File trunk = new File(contents, "trunk");
//        if (trunk.exists()) {
//          File file = new File(trunk, relativePath);
//          if (file.exists()) {
//            return file;
//          } else {
//            return null;
//          }
//        } else {
//          // check two levels deeper just in case
//          for (File nextLevel : contents.listFiles()) {
//            if (nextLevel.isDirectory()) {
//              File lowerTrunk = new File(nextLevel, "trunk");
//              if (lowerTrunk.exists()) {
//                File file = new File(lowerTrunk, relativePath);
//                if (file.exists()) {
//                  return file;
//                }
//              } else {
//                for (File nextNextLevel : nextLevel.listFiles()) {
//                  if (nextNextLevel.isDirectory()) {
//                    lowerTrunk = new File(nextNextLevel, "trunk");
//                    if (lowerTrunk.exists()) {
//                      File file = new File(lowerTrunk, relativePath);
//                      if (file.exists()) {
//                        return file;
//                      }
//                    }
//                  }
//                }
//              }
//            }
//          }
          File file = new File(repo + File.separatorChar + relativePath);
          if (file.exists()) {
            return file;
          } else {
            return null;
          }
//        }
//      }
//    }
//    return null;
  }

  public static Collection<RepoProject> getProjects(String repo) {
    Collection<RepoProject> projects = Helper.newLinkedList();
    for (File batch : new File(repo).listFiles()) {
      if (batch.isDirectory()) {
        for (File checkout : batch.listFiles()) {
          File contents = new File(checkout, "content");
          if (contents.exists()) {
            File trunk = new File(contents, "trunk");
            RepoProject project = new RepoProject(batch.getName(), checkout.getName());
            if (trunk.exists()) {
              project.addPath(trunk);
            } else {
              // check two levels deeper just in case
              for (File nextLevel : contents.listFiles()) {
                if (nextLevel.isDirectory()) {
                  File lowerTrunk = new File(nextLevel, "trunk");
                  if (lowerTrunk.exists()) {
                    project.addPath(lowerTrunk);
                  } else {
                    for (File nextNextLevel : nextLevel.listFiles()) {
                      if (nextNextLevel.isDirectory()) {
                        lowerTrunk = new File(nextNextLevel, "trunk");
                        if (lowerTrunk.exists()) {
                          project.addPath(lowerTrunk);
                        }
                      }
                    }
                  }
                }
              }
              if (project.getPaths().isEmpty()) {
                project.addPath(contents);
              }
            }
            projects.add(project);
          }
        }
      }
    }
    return projects;
  }
  
  public static Collection<File> getExtractedProjects(String repo) {
    Collection<File> projects = Helper.newLinkedList();
    for (File batch : new File(repo).listFiles()) {
      if (batch.isDirectory() && batch.getName().matches("\\d*")) {
        for (File checkout : batch.listFiles()) {
          projects.add(checkout);
        }
      }
    }
    return projects;
  }
  
  private static Collection<File> getJarFiles(File root) {
    Collection<File> jars = Helper.newLinkedList();
    Deque<File> fileStack = Helper.newStack();
    fileStack.add(root);
    while (!fileStack.isEmpty()) {
      root = fileStack.pop();
      if (root.exists()) {
        for (File file : root.listFiles()) {
          if (file.isDirectory()) {
            fileStack.push(file);
          } else if (file.getName().endsWith(".jar")) {
            jars.add(file);
          }
        }
      }
    }
    return jars;
  }
  
  public static RepoFileSet getFileSet(String roots) {
    Collection<File> rootFiles = Helper.newLinkedList();
    for (String root : roots.split(";")) {
      rootFiles.add(new File(root));
    }
    return getFileSet(rootFiles);
  }
  
  public static RepoFileSet getFileSet(Collection<File> roots) {
    RepoFileSet fileSet = new RepoFileSet(roots);
    Deque<File> fileStack = Helper.newStack();
    fileStack.addAll(roots);
    while (!fileStack.isEmpty()) {
      File root = fileStack.pop();
      if (root.exists()) {
        for (File file : root.listFiles()) {
          if (file.isDirectory()) {
            fileStack.push(file);
          } else if (file.getName().endsWith(".jar")) {
            fileSet.addJarFile(file);
          } else if (file.getName().endsWith(".java")) {
            fileSet.addJavaFile(file);
          }
        }
      }
    }
    return fileSet;
  }
  
  public static String getRepoPath(File outputDir) {
    return outputDir.getParentFile().getName() + "/" + outputDir.getName();
  }
  
  public static File getPropertiesFile(String repoRoot, File outputDir) {
    return new File(repoRoot, getRepoPath(outputDir) + File.separatorChar + "project.properties");
  }
  
  public static void main(String[] args) {
    PropertyManager properties = PropertyManager.getProperties(args);
    Logging.initializeLogger(properties);
    if (properties.isSet(Property.BUILD_JAR_INDEX)) {
      buildJarIndex(properties.getValue(Property.REPO_ROOT));
    } else if (properties.isSet(Property.CALCULATE_JAR_NAME_SIMILARITY)) {
      calculateJarNameSimilarity(properties.getValue(Property.REPO_ROOT));
    }
  }
}
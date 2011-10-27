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
package edu.uci.ics.sourcerer.extractor;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.IClassFile;

import edu.uci.ics.sourcerer.extractor.ast.FeatureExtractor;
import edu.uci.ics.sourcerer.extractor.io.IJarFileWriter;
import edu.uci.ics.sourcerer.extractor.io.WriterFactory;
import edu.uci.ics.sourcerer.extractor.io.dummy.DummyJarFileWriter;
import edu.uci.ics.sourcerer.extractor.resources.EclipseUtils;
import edu.uci.ics.sourcerer.repo.IndexedJar;
import edu.uci.ics.sourcerer.repo.RepoFileSet;
import edu.uci.ics.sourcerer.repo.RepoJar;
import edu.uci.ics.sourcerer.repo.RepoJavaFile;
import edu.uci.ics.sourcerer.repo.RepoProject;
import edu.uci.ics.sourcerer.repo.RepoTools;
import edu.uci.ics.sourcerer.util.io.Logging;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class Extractor implements IApplication {
  public static void extract(PropertyManager properties) {
    if (properties.isSet(Property.REPO_MODE)) {
      Set<String> completed = Logging.initializeResumeLogger(properties);
      
      String repo = properties.getValue(Property.REPO_ROOT);
      String baseOutput = properties.getValue(Property.OUTPUT);
      
      // Initialize EclipseUtils
      EclipseUtils.initialize();
      
      // Initialize the FeatureExtractor
      FeatureExtractor extractor = new FeatureExtractor(); 
      extractor.setPPA(properties.isSet(Property.PPA));
      
      // Start with the system jars
      logger.info("Getting the library jars");
      Collection<IPath> libraryJars = EclipseUtils.getLibraryJars();
      logger.info("Extracting " + libraryJars.size() + " library jars");
      int count = 0;
      for (IPath library : libraryJars) {
        File lib = library.toFile();
        logger.info("-------------------------------");
        logger.info("Extracting " + lib.getName() + " (" + ++count + " of " + libraryJars.size() + ")");
        
        if (completed.contains("**" + lib.getName())) {
          logger.info("Library already completed!");
        } else {
          logger.info("Initializing project");
          EclipseUtils.initializeLibraryProject(library);
          
          logger.info("Getting class files");
          Collection<IClassFile> classFiles = EclipseUtils.getClassFiles();
          
          logger.info("Extracting " + classFiles.size() + " class files");
          // Set up the properties
          properties.setProperty(Property.OUTPUT, RepoTools.getLibraryOutputPath(baseOutput, lib));
          properties.setBooleanProperty(Property.NOT_JAR, false);
          extractor.setOutput(properties);
          
          // Extract!
          extractor.extractClassFiles(classFiles);
          
          // Close the output files
          extractor.close();
          
          // The jar is completed
          logger.log(Logging.RESUME, "**" + lib.getName());
        }
      }
      
      // Start by analyzing the jars
      logger.info("Getting the jar index");
      Map<String, IndexedJar> jarIndex = RepoTools.getJarIndex(repo);
      logger.info("Extracting " + jarIndex.size() + " jars");
      count = 0;
      for (IndexedJar jar : jarIndex.values()) {
        logger.info("-------------------------------");
        logger.info("Extracting " + jar.getName() + " (" + ++count + " of " + jarIndex.size() + ")");
        
        if (completed.contains(jar.getName())) {
          logger.info("Jar already completed!");
        } else {
          logger.info("Initializing project");
          EclipseUtils.initializeJarProject(jar);
          
          logger.info("Getting class files");
          Collection<IClassFile> classFiles = EclipseUtils.getClassFiles();
          
          logger.info("Extracting " + classFiles.size() + " class files");
          // Set up the properties
          properties.setProperty(Property.OUTPUT, RepoTools.getJarOutputPath(baseOutput, jar));
          properties.setBooleanProperty(Property.NOT_JAR, false);
          extractor.setOutput(properties);
          
          // Extract!
          extractor.extractClassFiles(classFiles);
          
          // Close the output files
          extractor.close();
          
          // The jar is completed
          logger.log(Logging.RESUME, jar.getName());
        }
      }
      
      // If jar only, terminate
      if (properties.isSet(Property.JAR_ONLY)) {
        logger.info("Done!");
        return;
      }
      
      // Now analyze the projects
      logger.info("Getting project list");
      Collection<RepoProject> projects = RepoTools.getProjects(repo);
      logger.info("Extracting " + projects.size() + " projects");
      count = 0;
      for (RepoProject project : projects) {
        logger.info("-------------------------------");
        logger.info("Extracting " + project.getProject() + " (" + ++count + " of " + projects.size() + ")");
        
        if (completed.contains(project.getProject())) {
          logger.info("Project already completed!");
        } else {
          logger.info("Getting file list");
          RepoFileSet files = RepoTools.getFileSet(project.getPaths());
          
          logger.info("Initializing project with " + files.getJarFiles().size() + " jar files");
          EclipseUtils.initializeProject(files.getJarFiles());
          
          Collection<RepoJavaFile> uniqueFiles = files.getUniqueJavaFiles();
          logger.info("Loading " + uniqueFiles.size() + " unique files into project");
          Collection<IFile> uniqueIFiles = EclipseUtils.loadFilesIntoProject(uniqueFiles);
          
          Collection<RepoJavaFile> bestDuplicateFiles = files.getBestDuplicateJavaFiles();
          logger.info("Loading " + bestDuplicateFiles.size() + " chosen duplicate files into project");
          Collection<IFile> bestDuplicateIFiles = EclipseUtils.loadFilesIntoProject(bestDuplicateFiles);
          
          // Set up the properties
          properties.setProperty(Property.OUTPUT, project.getOutputPath(baseOutput));
          properties.setBooleanProperty(Property.NOT_JAR, true);
          extractor.setOutput(properties);
          
          // Write out jar files
          IJarFileWriter jarWriter = WriterFactory.createWriter(properties, Property.JAR_FILE_WRITER, DummyJarFileWriter.class);
          for (File file : files.getJarFiles()) {
            String md5 = RepoJar.getHash(file);
            IndexedJar jar = jarIndex.get(md5);
            if (jar == null) {
              logger.log(Level.SEVERE, "Unknown jar file: " + file.getPath());
            } else {
              jarWriter.writeJarFile(jar.getName());
            }
          }
          jarWriter.close();
          
          logger.info("Extracting references for " + uniqueFiles.size() + " compilation units");
          extractor.extractSourceFiles(uniqueIFiles);
          
          logger.info("Extracting references for " + bestDuplicateFiles.size() + " compilation units");
          extractor.extractSourceFiles(bestDuplicateIFiles);
          
          extractor.close();
          
          logger.log(Logging.RESUME, project.getProject());
        }
      }
      logger.info("Done!");
    } else {
      Logging.initializeLogger(properties);
      
      // Initialize EclipseUtils
      EclipseUtils.initialize();
      
      logger.info("Getting file list");
      RepoFileSet files = RepoTools.getFileSet(properties.getValue(Property.INPUT));
      
      logger.info("Initializing project with " + files.getJarFiles().size() + " jar files");
      EclipseUtils.initializeProject(files.getJarFiles());
      
      Collection<RepoJavaFile> uniqueFiles = files.getUniqueJavaFiles();
      logger.info("Loading " + uniqueFiles.size() + " unique files into project");
      Collection<IFile> uniqueIFiles = EclipseUtils.loadFilesIntoProject(uniqueFiles);
      
      Collection<RepoJavaFile> bestDuplicateFiles = files.getBestDuplicateJavaFiles();
      logger.info("Loading " + bestDuplicateFiles.size() + " chosen duplicate files into project");
      Collection<IFile> bestDuplicateIFiles = EclipseUtils.loadFilesIntoProject(bestDuplicateFiles);
      
      FeatureExtractor extractor = new FeatureExtractor();
      
      properties.setBooleanProperty(Property.NOT_JAR, true);
      extractor.setOutput(properties);
      extractor.setPPA(properties.isSet(Property.PPA));
 
      logger.info("Extracting references for " + uniqueFiles.size() + " compilation units");
      extractor.extractSourceFiles(uniqueIFiles);
      
      logger.info("Extracting references for " + bestDuplicateFiles.size() + " compilation units");
      extractor.extractSourceFiles(bestDuplicateIFiles);
      
      extractor.close();

      logger.info("Done!");
    }
  }

  @Override
  public Object start(IApplicationContext context) throws Exception {
    String[] args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    PropertyManager properties = PropertyManager.getProperties(args);
    properties.setProperty(Property.IMPORT_WRITER, "edu.uci.ics.sourcerer.extractor.io.file.ImportWriter");
    properties.setProperty(Property.ENTITY_WRITER, "edu.uci.ics.sourcerer.extractor.io.file.EntityWriter");
    properties.setProperty(Property.RELATION_WRITER, "edu.uci.ics.sourcerer.extractor.io.file.RelationWriter");
    properties.setProperty(Property.COMMENT_WRITER, "edu.uci.ics.sourcerer.extractor.io.file.CommentWriter");
    properties.setProperty(Property.JAR_ENTITY_WRITER, "edu.uci.ics.sourcerer.extractor.io.file.JarEntityWriter");
    properties.setProperty(Property.FILE_WRITER, "edu.uci.ics.sourcerer.extractor.io.file.FileWriter");
    properties.setProperty(Property.JAR_FILE_WRITER, "edu.uci.ics.sourcerer.extractor.io.file.JarFileWriter");
    Extractor.extract(properties);
    return EXIT_OK;
  }

  @Override
  public void stop() {}
}

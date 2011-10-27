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
package edu.uci.ics.sourcerer.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import edu.uci.ics.sourcerer.util.Helper;

@SuppressWarnings("serial")
public abstract class Logging {
  public static final Level RESUME = new Level("RESUME", 10000) {};
  public static Logger logger = null;
  
  private static Set<String> getResumeSet(String resumeFile) {
    File file = new File(resumeFile);
    if (file.exists()) {
      Set<String> resumeSet = Helper.newHashSet();
      try {
        BufferedReader br = new BufferedReader(new FileReader(file));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
          resumeSet.add(line);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      return resumeSet;
    } else {
      return Collections.emptySet();
    }
  }
    
  public static Set<String> initializeResumeLogger(PropertyManager properties) {
    String resumeFile = properties.getValue(Property.OUTPUT) + File.separatorChar + properties.getValue(Property.RESUME_LOG);
    
    Set<String> resumeSet = getResumeSet(resumeFile);
    
    initializeLogger(properties);
    
    try {
      FileHandler resumeHandler = new FileHandler(resumeFile, true);
      resumeHandler.setLevel(RESUME);
      resumeHandler.setFormatter(new Formatter() {
        @Override
        public String format(LogRecord record) {
          return record.getMessage() + "\n";
        }
      });
      logger.addHandler(resumeHandler);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    
    return resumeSet;
  }
 
  public static void initializeLogger(PropertyManager properties) {
    logger = Logger.getLogger("edu.uci.ics.sourcerer.util.io");
    logger.setUseParentHandlers(false);
    
    try {
      final boolean reportToConsole = properties.isSet(Property.REPORT_TO_CONSOLE);
      final DateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
      
      File dir = new File(properties.getValue(Property.OUTPUT));
      dir.mkdirs();
      
      FileHandler errorHandler = new FileHandler(properties.getValue(Property.OUTPUT) + File.separatorChar + properties.getValue(Property.ERROR_LOG));
      errorHandler.setLevel(Level.WARNING);
      errorHandler.setFormatter(new Formatter() {
        @Override
        public String format(LogRecord record) {
          if (record.getLevel() == RESUME) {
            return "";
          } else {
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            pw.print("[" + format.format(new Date(record.getMillis())) + " - " + record.getLevel() + "] ");
            pw.print(record.getMessage());
            if (record.getThrown() != null) {
              pw.println();
              record.getThrown().printStackTrace(pw);
            }
            pw.println();
            
            if (reportToConsole && record.getLevel() == Level.SEVERE) {
              System.err.print(writer.toString());
            }
            return writer.toString();
          }
        }
      });
      
      
      FileHandler infoHandler = new FileHandler(properties.getValue(Property.OUTPUT) + File.separatorChar + properties.getValue(Property.INFO_LOG));
      infoHandler.setLevel(Level.INFO);
      infoHandler.setFormatter(new Formatter() {
        @Override
        public String format(LogRecord record) {
          if (record.getLevel() == Level.INFO) {
            String output = "[" + format.format(new Date(record.getMillis())) + "] " + record.getMessage() + "\n";
            if (reportToConsole) {
              System.out.print(output);
            }
            return output;
          } else {
            return "";
          }
        }
      });
      
      logger.addHandler(errorHandler);
      logger.addHandler(infoHandler);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
  
  public static void initializeLogger() {
    logger = Logger.getLogger("edu.uci.ics.sourcerer.util.io");
    logger.setUseParentHandlers(false);

    final DateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");

    ConsoleHandler errorHandler = new ConsoleHandler();
    errorHandler.setLevel(Level.WARNING);
    errorHandler.setFormatter(new Formatter() {
      @Override
      public String format(LogRecord record) {
        if (record.getLevel() == RESUME) {
          return "";
        } else {
          StringWriter writer = new StringWriter();
          PrintWriter pw = new PrintWriter(System.err);
          pw.print("[" + format.format(new Date(record.getMillis())) + " - "
              + record.getLevel() + "] ");
          pw.print(record.getMessage());
          if (record.getThrown() != null) {
            pw.println();
            record.getThrown().printStackTrace(pw);
          }
          pw.println();

          return writer.toString();
        }
      }
    });

    ConsoleHandler infoHandler = new ConsoleHandler();
    infoHandler.setLevel(Level.INFO);
    infoHandler.setFormatter(new Formatter() {
      @Override
      public String format(LogRecord record) {
        if (record.getLevel() == Level.INFO) {
          String output = "[" + format.format(new Date(record.getMillis()))
              + "] " + record.getMessage() + "\n";
          return output;
        } else {
          return "";
        }
      }
    });

    logger.addHandler(errorHandler);
    logger.addHandler(infoHandler);
  }
}

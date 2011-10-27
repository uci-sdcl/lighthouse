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
package edu.uci.ics.sourcerer.extractor.io.model;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;

import edu.uci.ics.sourcerer.util.Helper;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class JarFileReader {
  public static Collection<String> getJarFiles(PropertyManager properties) {
    Collection<String> jarFiles = Helper.newLinkedList();
    try {
      BufferedReader br = new BufferedReader(new FileReader(new File(properties.getValue(Property.INPUT), properties.getValue(Property.JAR_FILE_FILE))));
      for (String line = br.readLine(); line != null; line = br.readLine()) {
        jarFiles.add(line);
      }
      return jarFiles;
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Unable to read jar file list", e);
      jarFiles.clear();
      return jarFiles;
    }
  }
}

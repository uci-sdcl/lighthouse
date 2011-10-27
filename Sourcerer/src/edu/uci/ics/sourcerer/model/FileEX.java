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

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

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.util.logging.Level;
public final class EntityEX {
  public static String getJarLine(Entity type, String fqn, int modifiers) {
    return type.name() + " " + fqn + " " + modifiers;
  }
  
  public static EntityEX getJarEntity(String line) {
    String[] parts = line.split(" ");
    
    try {
      if (parts.length == 3) {
        return new EntityEX(Entity.valueOf(parts[0]), parts[1], parts[2]);
      } else {
        logger.log(Level.SEVERE, "Unable to parse line: " + line);
        return null;
      }
    } catch (IllegalArgumentException e) {
      logger.log(Level.SEVERE, "Unable to parse line: " + line);
      return null;
    }
  }
  
  public static String getLine(Entity type, String fqn, int modifiers, String filename, int startPos, int length) {
    return type.name() + " " + fqn + " " + modifiers + " " + filename + " " + startPos + " " + length;
  }
  
  public static EntityEX getEntity(String line) {
    String[] parts = line.split(" ");
    
    try {
      return new EntityEX(Entity.valueOf(parts[0]), parts[1], parts[2], parts[3], parts[4], parts[5]);
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.log(Level.SEVERE, "Unable to parse line: " + line);
      return null;
    } catch (IllegalArgumentException e) {
      logger.log(Level.SEVERE, "Unable to parse line: " + line);
      return null;
    }
  }
  
  private Entity type;
  private String fqn;
  private String mods;
  private String path;
  private String offset;
  private String length;
  
  private EntityEX(Entity type, String fqn, String mods) {
    this(type, fqn, mods, null, null, null);
  }
  
  private EntityEX(Entity type, String fqn, String mods, String path, String offset, String length) {
    this.type = type;
    this.fqn = fqn;
    this.mods = mods;
    this.path = path;
    this.offset = offset;
    this.length = length;
  }
  
  public Entity getType() {
    return type;
  }

  public String getFqn() {
    return fqn;
  }

  public String getMods() {
    return mods;
  }

  public String getPath() {
    return path;
  }

  public String getOffset() {
    return offset;
  }

  public String getLength() {
    return length;
  }
}

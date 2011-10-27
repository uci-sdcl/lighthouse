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

public class RelationEX {
  public static RelationEX getJarRelation(String line) {
    String[] parts = line.split(" ");
    
    try {
      Relation type = Relation.valueOf(parts[0]);
      switch (type) {
        case INSIDE:
        case EXTENDS:
        case IMPLEMENTS:
        case HOLDS:
        case RETURNS:
          if (parts.length == 3) {
            return new RelationEX(type, parts[1], parts[2]);
          } else {
            return null;
          }
        case RECEIVES:
          if (parts.length == 5) {
            return new RelationEX(type, parts[1], parts[2], parts[3], parts[4]);
          } else {
            return null;
          }
        default:
          return null;
      }
    } catch (IllegalArgumentException e) {
      logger.log(Level.SEVERE, "Unable to parse line: " + line);
      return null;
    }
  }
  
  public static String getLine(Relation type, String lhs, String rhs) {
    return type.name() + " " + lhs + " " + rhs;
  }
  
  public static String getLineReceives(String lhs, String rhs, String paramName, int position) {
    return Relation.RECEIVES + " " + lhs + " " + rhs + " " + paramName + " " + position;
  }
  
  public static String getLineParametrizedBy(String lhs, String rhs, int position) {
    return Relation.PARAMETRIZED_BY + " " + lhs + " " + rhs + " " + position;
  }
  
  public static RelationEX getRelation(String line) {
    String[] parts = line.split(" ");
    
    try {
      Relation type = Relation.valueOf(parts[0]);
      if (type == Relation.RECEIVES) {
        return new RelationEX(type, parts[1], parts[2], parts[3], parts[4]);
      } else if (type == Relation.PARAMETRIZED_BY) {
        return new RelationEX(type, parts[1], parts[2], parts[3]);
      } else {
        return new RelationEX(type, parts[1], parts[2]);
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.log(Level.SEVERE, "Unable to parse line: " + line);
      return null;
    } catch (IllegalArgumentException e) {
      logger.log(Level.SEVERE, "Unable to parse line: " + line);
      return null;
    }
  }

  private Relation type;
  private String lhs;
  private String rhs;
  private String paramName;
  private String paramPos;
  
  private RelationEX(Relation type, String lhsEid, String rhsEid) {
    this(type, lhsEid, rhsEid, null, null);
  }
  
  private RelationEX(Relation type, String lhs, String rhs, String paramName, String paramPos) {
    this.type = type;
    this.lhs = lhs;
    this.rhs = rhs;
    this.paramName = paramName;
    this.paramPos = paramPos;
  }
  
  private RelationEX(Relation type, String lhs, String rhs, String paramPos) {
    this.type = type;
    this.lhs = lhs;
    this.rhs = rhs;
    this.paramPos = paramPos;
    this.paramName = rhs.substring(1, rhs.length() - 1);
  }
  
  public Relation getType() {
    return type;
  }

  public String getLhs() {
    return lhs;
  }

  public String getRhs() {
    return rhs;
  }
  
  public String getParamName() {
    return paramName;
  }
  
  public String getParamPos() {
    return paramPos;
  }
  
  public int hashCode() {
    return (lhs + rhs).hashCode();
  }
  
  public boolean equals(Object o) {
    if (o instanceof RelationEX) {
      RelationEX other = (RelationEX)o;
      if (type == Relation.RECEIVES) {
        return type.equals(other.type) && lhs.equals(other.lhs) && rhs.equals(other.rhs) && paramName.equals(other.paramName) && paramPos.equals(other.paramPos);
      } else {
        return type.equals(other.type) && lhs.equals(other.lhs) && rhs.equals(other.rhs);
      }
    } else {
      return false;
    }
  }
}

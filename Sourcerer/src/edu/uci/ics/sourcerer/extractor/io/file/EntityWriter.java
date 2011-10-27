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
package edu.uci.ics.sourcerer.extractor.io.file;

import edu.uci.ics.sourcerer.extractor.io.IEntityWriter;
import edu.uci.ics.sourcerer.model.Entity;
import edu.uci.ics.sourcerer.model.EntityEX;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public final class EntityWriter extends ExtractorWriter implements IEntityWriter {
  public EntityWriter(PropertyManager properties) {
    super(properties, Property.ENTITY_FILE);
  }

  public void writeClass(String fqn, int modifiers, String filename, int startPos, int length) {
    write(EntityEX.getLine(Entity.CLASS, fqn, modifiers, convertToRelativePath(filename), startPos, length));
  }

  public void writeInterface(String fqn, int modifiers, String filename, int startPos, int length) {
    write(EntityEX.getLine(Entity.INTERFACE, fqn, modifiers, convertToRelativePath(filename), startPos, length));
  }
  
  public void writeInitializer(String fqn, int modifiers, String filename, int startPos, int length) {
    write(EntityEX.getLine(Entity.INITIALIZER, fqn, modifiers, convertToRelativePath(filename), startPos, length));
  }
  
  public void writeConstructor(String fqn, int modifiers, String filename, int startPos, int length) {
    write(EntityEX.getLine(Entity.CONSTRUCTOR, fqn, modifiers, convertToRelativePath(filename), startPos, length));
  }
  
  public void writeEnum(String fqn, int modifiers, String filename, int startPos, int length) {
    write(EntityEX.getLine(Entity.ENUM, fqn, modifiers, convertToRelativePath(filename), startPos, length));
  }
  
  public void writeMethod(String fqn, int modifiers, String filename, int startPos, int length) {
    write(EntityEX.getLine(Entity.METHOD, fqn, modifiers, convertToRelativePath(filename), startPos, length));
  }
  
  public void writeField(String fqn, int modifiers, String filename, int startPos, int length) {
    write(EntityEX.getLine(Entity.FIELD, fqn, modifiers, convertToRelativePath(filename), startPos, length));
  }
  
  public void writeEnumConstant(String fqn, int modifiers, String filename, int startPos, int length) {
    write(EntityEX.getLine(Entity.ENUM_CONSTANT, fqn, modifiers, convertToRelativePath(filename), startPos, length));    
  }
  
  public void writeAnnotation(String fqn, int modifiers, String filename, int startPos, int length) {
    write(EntityEX.getLine(Entity.ANNOTATION, fqn, modifiers, convertToRelativePath(filename), startPos, length));
  }
  
  public void writeAnnotationMember(String fqn, int modifiers, String filename, int startPos, int length) {
    write(EntityEX.getLine(Entity.ANNOTATION_ELEMENT, fqn, modifiers, convertToRelativePath(filename), startPos, length));
  }
}

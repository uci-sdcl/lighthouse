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

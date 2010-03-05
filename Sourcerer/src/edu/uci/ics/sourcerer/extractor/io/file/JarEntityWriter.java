package edu.uci.ics.sourcerer.extractor.io.file;

import edu.uci.ics.sourcerer.extractor.io.IJarEntityWriter;
import edu.uci.ics.sourcerer.model.Entity;
import edu.uci.ics.sourcerer.model.EntityEX;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public final class JarEntityWriter extends ExtractorWriter implements IJarEntityWriter {
  public JarEntityWriter(PropertyManager properties) {
    super (properties, Property.ENTITY_FILE);
  }
  
  public void writeClass(String fqn, int modifiers) {
    write(EntityEX.getJarLine(Entity.CLASS, fqn, modifiers));
  }
  
  public void writeInterface(String fqn, int modifiers) {
    write(EntityEX.getJarLine(Entity.INTERFACE, fqn, modifiers));
  }
  
  public void writeAnnotation(String fqn, int modifiers) {
    write(EntityEX.getJarLine(Entity.ANNOTATION, fqn, modifiers));
  }
  
  public void writeAnnotationElement(String fqn, int modifiers) {
    write(EntityEX.getJarLine(Entity.ANNOTATION_ELEMENT, fqn, modifiers));
  }
  
  public void writeEnum(String fqn, int modifiers) {
    write(EntityEX.getJarLine(Entity.ENUM, fqn, modifiers));
  }
  
  public void writeEnumConstant(String fqn, int modifiers) {
    write(EntityEX.getJarLine(Entity.ENUM_CONSTANT, fqn, modifiers));
  }
  
  public void writeField(String fqn, int modifiers) {
    write(EntityEX.getJarLine(Entity.FIELD, fqn, modifiers));
  }
  
  public void writeMethod(String fqn, int modifiers) {
    write(EntityEX.getJarLine(Entity.METHOD, fqn, modifiers));
  }
  
  public void writeConstructor(String fqn, int modifiers) {
    write(EntityEX.getJarLine(Entity.CONSTRUCTOR, fqn, modifiers));
  }
}

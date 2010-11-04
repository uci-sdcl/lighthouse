package edu.uci.ics.sourcerer.extractor.io.dummy;

import edu.uci.ics.sourcerer.extractor.io.IJarEntityWriter;

public class DummyJarEntityWriter implements IJarEntityWriter {
  @Override
  public void writeAnnotation(String fqn, int modifiers) {
  }

  @Override
  public void writeAnnotationElement(String fqn, int modifiers) {
  }

  @Override
  public void writeClass(String fqn, int modifiers) {
  }

  @Override
  public void writeConstructor(String fqn, int modifiers) {
  }

  @Override
  public void writeEnum(String fqn, int modifiers) {
  }

  @Override
  public void writeEnumConstant(String fqn, int modifiers) {
  }

  @Override
  public void writeField(String fqn, int modifiers) {
  }

  @Override
  public void writeInterface(String fqn, int modifiers) {
  }

  @Override
  public void writeMethod(String fqn, int modifiers) {
  }

  @Override
  public void close() {
  }
}

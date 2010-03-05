package edu.uci.ics.sourcerer.extractor.io.dummy;

import edu.uci.ics.sourcerer.extractor.io.IEntityWriter;

public class DummyEntityWriter implements IEntityWriter {
  @Override
  public void writeAnnotation(String fqn, int modifiers, String filename, int startPos, int length) {
  }

  @Override
  public void writeAnnotationMember(String fqn, int modifiers, String filename, int startPos, int length) {
  }

  @Override
  public void writeClass(String fqn, int modifiers, String filename, int startPos, int length) {
  }

  @Override
  public void writeConstructor(String fqn, int modifiers, String filename, int startPos, int length) {
  }

  @Override
  public void writeEnum(String fqn, int modifiers, String filename, int startPos, int length) {
  }

  @Override
  public void writeEnumConstant(String fqn, int modifiers, String filename, int startPos, int length) {
  }

  @Override
  public void writeField(String fqn, int modifiers, String filename, int startPos, int length) {
  }

  @Override
  public void writeInitializer(String fqn, int modifiers, String filename, int startPos, int length) {
  }

  @Override
  public void writeInterface(String fqn, int modifiers, String filename, int startPos, int length) {
  }

  @Override
  public void writeMethod(String fqn, int modifiers, String filename, int startPos, int length) {
  }

  @Override
  public void close() {
  }
}

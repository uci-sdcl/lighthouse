package edu.uci.ics.sourcerer.extractor.io;

public interface IEntityWriter extends IExtractorWriter {
  public void writeClass(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeInterface(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeInitializer(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeConstructor(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeEnum(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeMethod(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeField(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeEnumConstant(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeAnnotation(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeAnnotationMember(String fqn, int modifiers, String filename, int startPos, int length);
}
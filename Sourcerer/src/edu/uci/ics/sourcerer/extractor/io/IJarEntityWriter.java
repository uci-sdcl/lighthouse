package edu.uci.ics.sourcerer.extractor.io;

public interface IJarEntityWriter extends IExtractorWriter {

  public void writeClass(String fqn, int modifiers);

  public void writeInterface(String fqn, int modifiers);

  public void writeAnnotation(String fqn, int modifiers);

  public void writeAnnotationElement(String fqn, int modifiers);

  public void writeEnum(String fqn, int modifiers);

  public void writeEnumConstant(String fqn, int modifiers);

  public void writeField(String fqn, int modifiers);

  public void writeMethod(String fqn, int modifiers);

  public void writeConstructor(String fqn, int modifiers);

}
package edu.uci.ics.sourcerer.extractor.io;

public interface IRelationWriter extends IExtractorWriter {

  public void writeInside(String innerFqn, String outerFqn);

  public void writeExtends(String subTypeFqn, String superTypeFqn);

  public void writeImplements(String subTypeFqn, String superTypeFqn);

  public void writeReceives(String fqn, String paramType, String paramName, int position);

  public void writeThrows(String fqn, String exceptionType);

  public void writeReturns(String fqn, String returnType);

  public void writeHolds(String fqn, String type);

  public void writeUses(String fqn, String type);

  public void writeCalls(String caller, String callee);

  public void writeAccesses(String accessor, String field);

  public void writeAnnotates(String entity, String annotation);

  public void writeParametrizedBy(String fqn, String typeVariable, int pos);

}
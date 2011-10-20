package edu.uci.ics.sourcerer.extractor.io.dummy;

import edu.uci.ics.sourcerer.extractor.io.IRelationWriter;

public class DummyRelationWriter implements IRelationWriter {
  @Override
  public void writeAccesses(String accessor, String field) {
  }

  @Override
  public void writeAnnotates(String entity, String annotation) {
  }

  @Override
  public void writeCalls(String caller, String callee) {
  }

  @Override
  public void writeExtends(String subTypeFqn, String superTypeFqn) {
  }

  @Override
  public void writeHolds(String fqn, String type) {
  }

  @Override
  public void writeImplements(String subTypeFqn, String superTypeFqn) {
  }

  @Override
  public void writeInside(String innerFqn, String outerFqn) {
  }

  @Override
  public void writeParametrizedBy(String fqn, String typeVariable, int pos) {
  }

  @Override
  public void writeReceives(String fqn, String paramType, String paramName, int position) {
  }

  @Override
  public void writeReturns(String fqn, String returnType) {
  }

  @Override
  public void writeThrows(String fqn, String exceptionType) {
  }

  @Override
  public void writeUses(String fqn, String type) {
  }

  @Override
  public void close() {
  }
}

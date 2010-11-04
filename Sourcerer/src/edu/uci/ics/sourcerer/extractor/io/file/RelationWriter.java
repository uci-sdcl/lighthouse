package edu.uci.ics.sourcerer.extractor.io.file;

import edu.uci.ics.sourcerer.extractor.io.IRelationWriter;
import edu.uci.ics.sourcerer.model.Relation;
import edu.uci.ics.sourcerer.model.RelationEX;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class RelationWriter extends ExtractorWriter implements IRelationWriter {
  private boolean notJar;
  
  public RelationWriter(PropertyManager properties) {
    super(properties, Property.RELATION_FILE);
    this.notJar = properties.isSet(Property.NOT_JAR);
  }

  public void writeInside(String innerFqn, String outerFqn) {
    write(RelationEX.getLine(Relation.INSIDE, innerFqn, outerFqn));
  }
  
  public void writeExtends(String subTypeFqn, String superTypeFqn) {
    write(RelationEX.getLine(Relation.EXTENDS, subTypeFqn, superTypeFqn));
  }
  
  public void writeImplements(String subTypeFqn, String superTypeFqn) {
    write(RelationEX.getLine(Relation.IMPLEMENTS, subTypeFqn, superTypeFqn));
  }
  
  public void writeReceives(String fqn, String paramType, String paramName, int position) {
    write(RelationEX.getLineReceives(fqn, paramType, paramName, position));
    if (notJar) {
      writeUses(fqn, paramType);
    }
  }
  
  public void writeThrows(String fqn, String exceptionType) {
    write(RelationEX.getLine(Relation.THROWS, fqn, exceptionType));
    if (notJar) {
      writeUses(fqn, exceptionType);
    }
  }
  
  public void writeReturns(String fqn, String returnType) {
    write(RelationEX.getLine(Relation.RETURNS, fqn, returnType));
    if (notJar) {
      writeUses(fqn, returnType);
    }
  }
  
  public void writeHolds(String fqn, String type) {
    write(RelationEX.getLine(Relation.HOLDS, fqn, type));
    if (notJar) {
      writeUses(fqn, type);
    }
  }
  
  public void writeUses(String fqn, String type) {
    write(RelationEX.getLine(Relation.USES, fqn, type));
  }
  
  public void writeCalls(String caller, String callee) {
    write(RelationEX.getLine(Relation.CALLS, caller, callee));
  }
  
  public void writeAccesses(String accessor, String field) {
    write(RelationEX.getLine(Relation.ACCESSES, accessor, field));
  }
  
  public void writeAnnotates(String entity, String annotation) {
    write(RelationEX.getLine(Relation.ANNOTATED_BY, entity, annotation));
    if (notJar) {
      writeUses(entity, annotation);
    }
  }
  
  public void writeParametrizedBy(String fqn, String typeVariable, int pos) {
    write(RelationEX.getLineParametrizedBy(fqn, typeVariable, pos));
  }
}

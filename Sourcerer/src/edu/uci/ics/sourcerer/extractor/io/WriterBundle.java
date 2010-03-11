package edu.uci.ics.sourcerer.extractor.io;

import java.io.File;

import edu.uci.ics.sourcerer.extractor.io.dummy.DummyCommentWriter;
import edu.uci.ics.sourcerer.extractor.io.dummy.DummyEntityWriter;
import edu.uci.ics.sourcerer.extractor.io.dummy.DummyFileWriter;
import edu.uci.ics.sourcerer.extractor.io.dummy.DummyImportWriter;
import edu.uci.ics.sourcerer.extractor.io.dummy.DummyJarEntityWriter;
import edu.uci.ics.sourcerer.extractor.io.dummy.DummyRelationWriter;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class WriterBundle {
  private IImportWriter importWriter;
  private IEntityWriter entityWriter;
  private IJarEntityWriter jarEntityWriter;
  private IRelationWriter relationWriter;
  private ICommentWriter commentWriter;
  private IFileWriter fileWriter;

  private PropertyManager properties;
  
  public WriterBundle(PropertyManager properties) {
    File output = new File(properties.getValue(Property.OUTPUT));
    output.mkdirs();
    this.properties = properties;
  }
  
  public IImportWriter getImportWriter() {
    if (importWriter == null) {
      importWriter = WriterFactory.createWriter(properties, Property.IMPORT_WRITER, DummyImportWriter.class);
    }
    return importWriter;
  }
  
  public IEntityWriter getEntityWriter() {
    if (entityWriter == null) {
      entityWriter = WriterFactory.createWriter(properties, Property.ENTITY_WRITER, DummyEntityWriter.class);
    }
    return entityWriter;
  }
  
  public IJarEntityWriter getJarEntityWriter() {
    if (jarEntityWriter == null) {
      jarEntityWriter = WriterFactory.createWriter(properties, Property.JAR_ENTITY_WRITER, DummyJarEntityWriter.class);
    }
    return jarEntityWriter;
  }
  
  public IRelationWriter getRelationWriter() {
    if (relationWriter == null) {
      relationWriter = WriterFactory.createWriter(properties, Property.RELATION_WRITER, DummyRelationWriter.class);
    }
    return relationWriter;
  }
  
  public ICommentWriter getCommentWriter() {
    if (commentWriter == null) {
      commentWriter = WriterFactory.createWriter(properties, Property.COMMENT_WRITER, DummyCommentWriter.class);
    }
    return commentWriter;
  }
  
  public IFileWriter getFileWriter() {
    if (fileWriter == null) {
      fileWriter = WriterFactory.createWriter(properties, Property.FILE_WRITER, DummyFileWriter.class);
    }
    return fileWriter;
  }

  public void close() {
    if (importWriter != null) {
      importWriter.close();
    }
    if (entityWriter != null) {
      entityWriter.close();
    }
    if (jarEntityWriter != null) {
      jarEntityWriter.close();
    }
    if (relationWriter != null) {
      relationWriter.close();
    }
    if (commentWriter != null) {
      commentWriter.close();
    }
    if (fileWriter != null) {
      fileWriter.close();
    }
  }
}

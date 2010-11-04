package edu.uci.ics.sourcerer.extractor.io;

public interface IImportWriter extends IExtractorWriter {

  public void writeImport(String filename, String name, boolean isStatic, boolean onDemand);

}
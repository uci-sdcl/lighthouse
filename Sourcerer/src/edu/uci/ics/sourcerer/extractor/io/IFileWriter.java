package edu.uci.ics.sourcerer.extractor.io;

public interface IFileWriter extends IExtractorWriter {
  public void writeFile(String filename, int errorCount);
}
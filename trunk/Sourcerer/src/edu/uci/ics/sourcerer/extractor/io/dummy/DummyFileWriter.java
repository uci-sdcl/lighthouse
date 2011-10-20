package edu.uci.ics.sourcerer.extractor.io.dummy;

import edu.uci.ics.sourcerer.extractor.io.IFileWriter;

public class DummyFileWriter implements IFileWriter {

  @Override
  public void writeFile(String filename, int errorCount) {
  }

  @Override
  public void close() {
  }
}

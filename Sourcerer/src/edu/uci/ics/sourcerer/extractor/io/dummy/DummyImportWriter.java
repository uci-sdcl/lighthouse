package edu.uci.ics.sourcerer.extractor.io.dummy;

import edu.uci.ics.sourcerer.extractor.io.IImportWriter;

public class DummyImportWriter implements IImportWriter {
  @Override
  public void writeImport(String filename, String name, boolean isStatic, boolean onDemand) {
  }

  @Override
  public void close() {
  }
}

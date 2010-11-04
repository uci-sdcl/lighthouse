package edu.uci.ics.sourcerer.extractor.io.file;

import edu.uci.ics.sourcerer.extractor.io.IImportWriter;
import edu.uci.ics.sourcerer.model.ImportEX;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public final class ImportWriter extends ExtractorWriter implements IImportWriter {
  public ImportWriter(PropertyManager properties) {
    super(properties, Property.IMPORT_FILE);
  }
  
  public void writeImport(String filename, String name, boolean isStatic, boolean onDemand) {
    write(ImportEX.getLine(convertToRelativePath(filename), name, isStatic, onDemand));
  }
}

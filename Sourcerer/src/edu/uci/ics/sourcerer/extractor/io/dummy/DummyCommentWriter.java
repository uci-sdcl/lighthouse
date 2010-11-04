package edu.uci.ics.sourcerer.extractor.io.dummy;

import edu.uci.ics.sourcerer.extractor.io.ICommentWriter;

public class DummyCommentWriter implements ICommentWriter {
  @Override
  public void writeBlockComment(String containingFile, int startPos, int length) {
  }

  @Override
  public void writeJavadocComment(String containingFqn, int startPos, int length) {
  }

  @Override
  public void writeLineComment(String containingFile, int startPos, int length) {
  }

  @Override
  public void writeUnassociatedJavadocComment(String containingFile, int startPos, int length) {
  }

  @Override
  public void close() {
  }
}

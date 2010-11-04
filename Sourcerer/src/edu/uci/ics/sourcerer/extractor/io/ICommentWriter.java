package edu.uci.ics.sourcerer.extractor.io;

public interface ICommentWriter extends IExtractorWriter {
  public void writeLineComment(String containingFile, int startPos, int length);

  public void writeBlockComment(String containingFile, int startPos, int length);

  public void writeUnassociatedJavadocComment(String containingFile, int startPos, int length);

  public void writeJavadocComment(String containingFqn, int startPos, int length);

}
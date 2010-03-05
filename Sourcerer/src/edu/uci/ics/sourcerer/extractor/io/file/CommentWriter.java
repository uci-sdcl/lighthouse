package edu.uci.ics.sourcerer.extractor.io.file;

import edu.uci.ics.sourcerer.extractor.io.ICommentWriter;
import edu.uci.ics.sourcerer.model.Comment;
import edu.uci.ics.sourcerer.model.CommentEX;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class CommentWriter extends ExtractorWriter implements ICommentWriter {
  public CommentWriter(PropertyManager properties) {
    super(properties, Property.COMMENT_FILE);
  }

  public void writeLineComment(String containingFile, int startPos, int length) {
    write(CommentEX.getLine(Comment.LINE, convertToRelativePath(containingFile), startPos, length));
  }
  
  public void writeBlockComment(String containingFile, int startPos, int length) {
    write(CommentEX.getLine(Comment.BLOCK, convertToRelativePath(containingFile), startPos, length));
  }
  
  public void writeUnassociatedJavadocComment(String containingFile, int startPos, int length) {
    write(CommentEX.getLine(Comment.UJAVADOC, convertToRelativePath(containingFile), startPos, length));
  }
  
  public void writeJavadocComment(String containingFqn, int startPos, int length) {
    write(CommentEX.getLine(Comment.JAVADOC, containingFqn, startPos, length));
  }
}

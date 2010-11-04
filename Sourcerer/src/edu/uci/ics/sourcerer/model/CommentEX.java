package edu.uci.ics.sourcerer.model;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.util.logging.Level;

public class CommentEX {
  public static String getLine(Comment type, String path, int offset, int length) {
    return type.name() + " " + path + " " + offset + " " + length;
  }

  public static CommentEX getComment(String line) {
    String parts[] = line.split(" ");
    if (parts.length == 4) {
      try {
        return new CommentEX(Comment.valueOf(parts[0]), parts[1], parts[2], parts[3]);
      } catch (IllegalArgumentException e) {
        logger.log(Level.SEVERE, "Unable to parse line: " + line);
        return null;
      }
    } else {
      logger.log(Level.SEVERE, "Unable to parse line: " + line);
      return null;
    }
  }
  
  private Comment type;
  private String path;
  private String offset;
  private String length;
  
  private CommentEX(Comment type, String path, String offset, String length) {
    this.type = type;
    this.path = path;
    this.offset = offset;
    this.length = length;
  }

  public Comment getType() {
    return type;
  }

  public String getPath() {
    return path;
  }

  public String getOffset() {
    return offset;
  }

  public String getLength() {
    return length;
  }
}

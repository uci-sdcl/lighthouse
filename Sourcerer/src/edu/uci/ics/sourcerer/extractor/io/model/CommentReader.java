package edu.uci.ics.sourcerer.extractor.io.model;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.logging.Level;

import edu.uci.ics.sourcerer.model.CommentEX;
import edu.uci.ics.sourcerer.util.Helper;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class CommentReader implements Iterable<CommentEX> {
  private PropertyManager properties;
  
  private CommentReader(PropertyManager properties) {
    this.properties = properties;
  }
  
  public Iterator<CommentEX> iterator() {
    return getIterator(properties);
  }
  
  public static Iterable<CommentEX> getIterable(PropertyManager properties) {
    return new CommentReader(properties);
  } 
  
  private static Iterator<CommentEX> getIterator(PropertyManager properties) {
    try {
      final BufferedReader input = new BufferedReader(new FileReader(new File(properties.getValue(Property.INPUT), properties.getValue(Property.COMMENT_FILE))));
      return new Iterator<CommentEX>() {
        private static final int BATCH_SIZE = 10000;
        private Queue<CommentEX> queue = Helper.newQueue();
        private boolean isMore = true;
        
        private void readBatch() {
          try {
            for (int count = 0; count < BATCH_SIZE; count++) {
              String line = input.readLine();
              if (line == null) {
                isMore = false;
                return;
              } else {
                CommentEX comment = null;
                comment = CommentEX.getComment(line);
                if (comment != null) {
                  queue.add(comment);
                }
              }
            }
          } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to read line", e);
            isMore = false;
          }
        }
        
        @Override
        public boolean hasNext() {
          if (queue.isEmpty()) {
            if (isMore) {
              readBatch();
              return !queue.isEmpty();
            } else {
              return false;
            }
          } else {
            return true;
          }
        }

        @Override
        public CommentEX next() {
          if (queue.isEmpty()) {
            if (isMore) {
              readBatch();
              if (queue.isEmpty()) {
                throw new NoSuchElementException();
              } else {
                return queue.poll();
              }
            } else {
              throw new NoSuchElementException();
            }
          } else {
            return queue.poll();
          }
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    } catch (IOException e){
      logger.log(Level.SEVERE, "Error opening reader on file", e);
      return null;
    }
  }
}

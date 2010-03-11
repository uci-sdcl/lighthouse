package edu.uci.ics.sourcerer.extractor.io.model;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.logging.Level;

import edu.uci.ics.sourcerer.model.FileEX;
import edu.uci.ics.sourcerer.util.Helper;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class FileReader implements Iterable<FileEX> {
  private PropertyManager properties;
  
  private FileReader(PropertyManager properties) {
    this.properties = properties;
  }
  
  public Iterator<FileEX> iterator() {
    return getIterator(properties);
  }
    
  public static Iterable<FileEX> getIterable(PropertyManager properties) {
    return new FileReader(properties);
  } 
      
  private static Iterator<FileEX> getIterator(PropertyManager properties) {
    try {
      final BufferedReader input = new BufferedReader(new java.io.FileReader(new File(properties.getValue(Property.INPUT), properties.getValue(Property.FILE_FILE))));
      return new Iterator<FileEX>() {
        private static final int BATCH_SIZE = 10000;
        private Queue<FileEX> queue = Helper.newQueue();
        private boolean isMore = true;
        
        private void readBatch() {
          try {
            for (int count = 0; count < BATCH_SIZE; count++) {
              String line = input.readLine();
              if (line == null) {
                isMore = false;
                return;
              } else {
                FileEX file = null;
                file = FileEX.getFile(line);
                if (file != null) {
                  queue.add(file);
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
        public FileEX next() {
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

package edu.uci.ics.sourcerer.extractor.io.model;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.logging.Level;

import edu.uci.ics.sourcerer.model.RelationEX;
import edu.uci.ics.sourcerer.util.Helper;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class RelationReader implements Iterable<RelationEX> {
  private PropertyManager properties;
  private boolean jarMode;
  
  private RelationReader(PropertyManager properties, boolean jarMode) {
    this.properties = properties;
    this.jarMode = jarMode;
  }
  
  public Iterator<RelationEX> iterator() {
    if (jarMode) {
      return getJarIterator(properties);
    } else {
      return getIterator(properties);
    }
  }
  
  public static Iterable<RelationEX> getJarIterable(PropertyManager properties) {
    return new RelationReader(properties, true);
  }
  
  public static Iterable<RelationEX> getIterable(PropertyManager properties) {
    return new RelationReader(properties, false);
  } 
  
  public static Iterator<RelationEX> getJarIterator(PropertyManager properties) {
    return getIterator(properties, true);
  }
  
  public static Iterator<RelationEX> getIterator(PropertyManager properties) {
    return getIterator(properties, false);
  }
  
  private static Iterator<RelationEX> getIterator(PropertyManager properties, final boolean jarMode) {
    try {
      final BufferedReader input = new BufferedReader(new FileReader(new File(properties.getValue(Property.INPUT), properties.getValue(Property.RELATION_FILE))));
      return new Iterator<RelationEX>() {
        private LinkedHashSet<RelationEX> duplicateChecker = new LinkedHashSet<RelationEX>();
        private static final int BATCH_SIZE = 10000;
        private Queue<RelationEX> queue = Helper.newQueue();
        private boolean isMore = true;
        
        private void readBatch() {
          try {
            for (int count = 0; count < BATCH_SIZE; count++) {
              String line = input.readLine();
              if (line == null) {
                isMore = false;
                return;
              } else {
                RelationEX relation = null;
                if (jarMode) {
                  relation = RelationEX.getJarRelation(line);
                } else {
                  relation = RelationEX.getRelation(line);
                }
                if (relation != null) {
                  if (jarMode) {
                    queue.add(relation);
                  } else {
                    if (!duplicateChecker.contains(relation)) {
                      if (duplicateChecker.size() >= BATCH_SIZE) {
                        Iterator<RelationEX> iter = duplicateChecker.iterator();
                        for (int i = 0; i < BATCH_SIZE / 2; i++) {
                          iter.next();
                          iter.remove();
                        }
                      }
                      duplicateChecker.add(relation);
                      queue.add(relation);
                    }
                  }
                  
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
        public RelationEX next() {
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
      return new Iterator<RelationEX>(){
        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
      
        @Override
        public RelationEX next() {
          throw new NoSuchElementException();
        }
      
        @Override
        public boolean hasNext() {
          return false;
        }
      };
    }
  }
}

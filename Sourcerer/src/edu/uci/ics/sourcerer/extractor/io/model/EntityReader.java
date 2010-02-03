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

import edu.uci.ics.sourcerer.model.EntityEX;
import edu.uci.ics.sourcerer.util.Helper;
import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public class EntityReader implements Iterable<EntityEX> {
  private PropertyManager properties;
  private boolean jarMode;
  
  private EntityReader(PropertyManager properties, boolean jarMode) {
    this.properties = properties;
    this.jarMode = jarMode;
  }
  
  public Iterator<EntityEX> iterator() {
    if (jarMode) {
      return getJarIterator(properties);
    } else {
      return getIterator(properties);
    }
  }
  
  public static Iterable<EntityEX> getJarIterable(PropertyManager properties) {
    return new EntityReader(properties, true);
  }
  
  public static Iterable<EntityEX> getIterable(PropertyManager properties) {
    return new EntityReader(properties, false);
  } 
  
  public static Iterator<EntityEX> getJarIterator(PropertyManager properties) {
    return getIterator(properties, true);
  }
  
  public static Iterator<EntityEX> getIterator(PropertyManager properties) {
    return getIterator(properties, false);
  }
  
  private static Iterator<EntityEX> getIterator(PropertyManager properties, final boolean jarMode) {
    try {
      final BufferedReader input = new BufferedReader(new FileReader(new File(properties.getValue(Property.INPUT), properties.getValue(Property.ENTITY_FILE))));
      return new Iterator<EntityEX>() {
        private static final int BATCH_SIZE = 10000;
        private Queue<EntityEX> queue = Helper.newQueue();
        private boolean isMore = true;
        
        private void readBatch() {
          try {
            for (int count = 0; count < BATCH_SIZE; count++) {
              String line = input.readLine();
              if (line == null) {
                isMore = false;
                return;
              } else {
                EntityEX entity = null;
                if (jarMode) {
                  entity = EntityEX.getJarEntity(line);
                } else {
                  entity = EntityEX.getEntity(line);
                }
                if (entity != null) {
                  queue.add(entity);
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
        public EntityEX next() {
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
      return new Iterator<EntityEX>(){
        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
      
        @Override
        public EntityEX next() {
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

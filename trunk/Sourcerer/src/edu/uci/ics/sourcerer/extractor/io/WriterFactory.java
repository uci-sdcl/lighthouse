package edu.uci.ics.sourcerer.extractor.io;

import static edu.uci.ics.sourcerer.util.io.Logging.logger;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import edu.uci.ics.sourcerer.util.io.Property;
import edu.uci.ics.sourcerer.util.io.PropertyManager;

public final class WriterFactory {
  private WriterFactory() {}
  
  @SuppressWarnings("unchecked")
  public static <T> T createWriter(PropertyManager properties, Property property, Class<?> backup) {
    String name = properties.getValue(property);
    if (name != null) {
      try {
        Class<?> klass = Class.forName(name);
        Constructor<?> constructor = klass.getConstructor(PropertyManager.class);
        return (T)constructor.newInstance(properties);
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Unable to create writer: " + name);
      }
    }
    try {
      return (T)backup.newInstance();
    } catch (Exception e2) {
      logger.log(Level.SEVERE, "Unable to create backup writer!");
      return null;
    }
  }
}

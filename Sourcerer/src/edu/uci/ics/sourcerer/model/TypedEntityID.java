package edu.uci.ics.sourcerer.model;

public class TypedEntityID {
  public enum Type {
    LIBRARY,
    JAR,
    SOURCE;
    
    public boolean isSource() {
      return this == SOURCE;
    }
    
    public boolean isJar() {
      return this == JAR;
    }
  }
  
  private Type type;
  private String id;
  
  private TypedEntityID(Type type, String id) {
    this.type = type;
    this.id = id;
  }
  
  public Type getType() {
    return type;
  }
  
  public String getID() {
    return id;
  }
  
  public void setID(String id) {
    this.id = id;
  }
  
  public static TypedEntityID getLibraryEntityID(String id) {
    return new TypedEntityID(Type.LIBRARY, id); 
  }
  
  public static TypedEntityID getJarEntityID(String id) {
    return new TypedEntityID(Type.JAR, id); 
  }
  
  public static TypedEntityID getSourceEntityID(String id) {
    return new TypedEntityID(Type.SOURCE, id); 
  }
  
  public static TypedEntityID getEntityID(Type type, String id) {
    return new TypedEntityID(Type.SOURCE, id);
  }
  
  public int hashCode() {
    return id.hashCode();
  }
  
  public boolean equals(Object o) {
    if (o instanceof TypedEntityID) {
      TypedEntityID other = (TypedEntityID)o;
      return type.equals(other.type) && id.equals(other.id);
    } else {
      return false;
    }
  }
  
  public String toString() {
    return type.name() + "(" + id + ")"; 
  }
}

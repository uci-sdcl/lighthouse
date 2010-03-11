package edu.uci.ics.sourcerer.model;

import java.util.Set;

public class EntityDB {
  private TypedEntityID entityID;
  private Entity entityType;
  private String fqn;
  private String modifiers;

  public EntityDB(TypedEntityID entityID, Entity entityType, String fqn, String modifiers) {
    this.entityID = entityID;
    this.entityType = entityType;
    this.fqn = fqn;
    this.modifiers = modifiers;
  }

  public boolean isFromSource() {
    return entityID.getType().isSource();
  }

  public TypedEntityID getEntityID() {
    return entityID;
  }

  public Entity getEntityType() {
    return entityType;
  }

  public String getFqn() {
    return fqn;
  }

  public String getModifiers() {
    return modifiers;
  }

  public Set<Modifier> getModifiersSet() {
    return Modifier.convertFromString(modifiers);
  }
  
  public String toString() {
    return entityID.toString() + " " + fqn;
  }
}

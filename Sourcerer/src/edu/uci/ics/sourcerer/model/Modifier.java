package edu.uci.ics.sourcerer.model;

import java.util.Set;

import edu.uci.ics.sourcerer.util.Helper;

public enum Modifier {
  PUBLIC(0x0001),
  PRIVATE(0x0002),
  PROTECTED(0x0004),
  STATIC(0x0008),
  FINAL(0x0010),
  SYNCHRONIZED(0x0020),
  VOLATILE(0x0040),
  TRANSIENT(0x0080),
  NATIVE(0x0100),
  ABSTRACT(0x0400),
  STRICTFP(0x0800);
  
  private final int value;
  
  private Modifier(int value) {
    this.value = value;
  }

  public String toString() {
    return name().toLowerCase();
  }
  
  public static boolean is(Modifier type, String modifier) {
    return is(type, Integer.parseInt(modifier));
  }
  
  public static boolean is(Modifier type, int modifier) {
    return (modifier & type.value) == type.value;
  }
  
  public static int convertToInt(Set<Modifier> modifiers) {
    int value = 0;
    for (Modifier mod : modifiers) {
      value |= mod.value;
    }
    return value;
  }
  
  public static String convertToString(Set<Modifier> modifiers) {
    return Integer.toString(convertToInt(modifiers));
  }
  
  public static Set<Modifier> convertFromInt(int modifiers) {
    Set<Modifier> mods = Helper.newHashSet();
    for (Modifier mod : values()) {
      if ((mod.value & modifiers) == mod.value) {
        mods.add(mod);
      }
    }
    return mods;
  }
  
  public static Set<Modifier> convertFromString(String modifiers) {
    if (modifiers == null) {
      return null;
    } else {
      return convertFromInt(Integer.parseInt(modifiers));
    }
  }
}

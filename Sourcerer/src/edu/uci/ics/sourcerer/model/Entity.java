/**
 * 
 */
package edu.uci.ics.sourcerer.model;

public enum Entity {
    PACKAGE,
    CLASS,
    INTERFACE,
    CONSTRUCTOR,
    METHOD,
    INITIALIZER,
    FIELD,
    ENUM,
    ENUM_CONSTANT,
    ANNOTATION,
    ANNOTATION_ELEMENT,
    PRIMITIVE,
    ARRAY,
    TYPE_VARIABLE,
    PARAMETRIZED_TYPE,
    UNKNOWN;

    public boolean isDeclaredType() {
      return this == CLASS || this == INTERFACE || this == ENUM || this == ANNOTATION;
    }

    public boolean isPackage() {
      return this == PACKAGE;
    }

    public boolean isAnnotation() {
      return this == ANNOTATION;
    }

    public boolean isInitializer() {
      return this == INITIALIZER;
    }
    
    public boolean isInterface() {
      return this == INTERFACE;
    }

    public boolean isEnum() {
      return this == ENUM;
    }

    public boolean isClass() {
      return this == CLASS;
    }

    public boolean isArray() {
      return this == ARRAY;
    }

    public boolean isParametrizedType() {
      return this == PARAMETRIZED_TYPE;
    }

    public boolean isCallableType() {
      return this == METHOD || this == CONSTRUCTOR;
    }

    public boolean isMethod() {
      return this == METHOD;
    }

    public boolean isConstructor() {
      return this == CONSTRUCTOR;
    }

    public boolean isUnknown() {
      return this == UNKNOWN;
    }

    public boolean isFieldImport() {
      return this == FIELD || this == ENUM_CONSTANT;
    }

    public boolean isPrimitive() {
      return this == PRIMITIVE;
    }

    public boolean isImportable() {
      return this != PRIMITIVE && this != UNKNOWN;
    }
  }
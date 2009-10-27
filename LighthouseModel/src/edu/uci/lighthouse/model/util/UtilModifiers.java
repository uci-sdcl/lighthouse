package edu.uci.lighthouse.model.util;

public enum UtilModifiers {
	PUBLIC, PRIVATE, PROTECTED, STATIC, FINAL, SYNCHRONIZED, VOLATILE, TRANSIENT, NATIVE, ABSTRACT, STRICTFP;
	
	public String toString() {
		return name().toLowerCase();
	}
	
	public static boolean isPublic(String fqn) {
	    return (PUBLIC.toString().equals(fqn));
	}
	
	public static boolean isPrivate(String fqn) {
	    return (PRIVATE.toString().equals(fqn));
	}
	
	public static boolean isProtected(String fqn) {
	    return (PROTECTED.toString().equals(fqn));
	}
	
	public static boolean isStatic(String fqn) {
	    return (STATIC.toString().equals(fqn));
	}
	
	public static boolean isFinal(String fqn) {
	    return (FINAL.toString().equals(fqn));
	}
	
	public static boolean isSynchronized(String fqn) {
	    return (SYNCHRONIZED.toString().equals(fqn));
	}
	
	public static boolean isVolatile(String fqn) {
	    return (VOLATILE.toString().equals(fqn));
	}
	
	public static boolean isTransient(String fqn) {
	    return (TRANSIENT.toString().equals(fqn));
	}
	
	public static boolean isNative(String fqn) {
	    return (NATIVE.toString().equals(fqn));
	}
	
	public static boolean isAbstract(String fqn) {
	    return (ABSTRACT.toString().equals(fqn));
	}
	
	public static boolean isStrictfp(String fqn) {
	    return (STRICTFP.toString().equals(fqn));
	}	
}

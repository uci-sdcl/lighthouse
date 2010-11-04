package edu.uci.ics.sourcerer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Helper {
  public static <T> List<T> newArrayList(int initialCapacity) {
    return new ArrayList<T>(initialCapacity);
  }
  
  public static <T> List<T> newArrayList(Collection<? extends T> c) {
    return new ArrayList<T>(c);
  }
  
  public static <T> List<T> newArrayList() {
    return new ArrayList<T>();
  }
  
	public static <T> List<T> newLinkedList() {
		return new LinkedList<T>();
	}
	
	public static <K extends Enum<K>,V> Map<K,V> newEnumMap(Class<K> klass) {
	  return new EnumMap<K, V>(klass);
	}
	
	public static <K,V> Map<K,V> newHashMap() {
		return new HashMap<K, V>();
	}
	
	public static <K,V> Map<K,V> newTreeMap() {
	  return new TreeMap<K, V>();
	}
	
	public static <T> Set<T> newHashSet() {
	  return new HashSet<T>();
	}
	
	public static <T> TreeSet<T> newTreeSet() {
    return new TreeSet<T>();
  }
	
	public static <T> TreeSet<T> newTreeSet(Comparator<? super T> comp) {
	  return new TreeSet<T>(comp);
	}
		
	public static <T> Deque<T> newStack() {
	  return new LinkedList<T>();
	}
	
	public static <T> Queue<T> newQueue() {
	  return new LinkedList<T>();
	}
}

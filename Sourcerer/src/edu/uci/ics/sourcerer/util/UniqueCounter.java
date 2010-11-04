package edu.uci.ics.sourcerer.util;

import java.util.Comparator;
import java.util.Set;

public class UniqueCounter <E, T extends Comparable<T>> {
  private Set<E> values = null; 
  private T object = null;
  
  public UniqueCounter(T object) {
    this.object = object;
    values = Helper.newHashSet();
  }
  
  public void add(E value) {
    values.add(value);
  }
  
  public int getCount() {
    return values.size();
  }
  
  public T getObject() {
    return object;
  }
  
  public static <E, T extends Comparable<T>> Comparator<UniqueCounter<E, T>> getReverseComparator() {
    return new Comparator<UniqueCounter<E, T>>() {
      @Override
      public int compare(UniqueCounter<E, T> o1, UniqueCounter<E, T> o2) {
        if (o1.values.size() == o2.values.size()) {
          return o1.object.compareTo(o2.object);
        } else {
          return o2.values.size() - o1.values.size();
        }
      }
    };
  }
}

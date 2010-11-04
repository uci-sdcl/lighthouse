package edu.uci.ics.sourcerer.util;

import java.util.Comparator;

public class Counter <T extends Comparable<T>> {
  private int counter = 0;
  private T object = null;
  
  public Counter() {}
  
  public Counter(T object) {
    this.object = object;
  }
  
  public Counter(T object, int count) {
    this.object = object;
    this.counter = count;
  }
  
  public void add(int value) {
    counter += value;
  }
  
  public void increment() {
    counter++;
  }
  
  public int getCount() {
    return counter;
  }
  
  public T getObject() {
    return object;
  }
  
  public static <T extends Comparable<T>> Comparator<Counter<T>> getReverseComparator() {
    return new Comparator<Counter<T>>() {
      @Override
      public int compare(Counter<T> o1, Counter<T> o2) {
        if (o1.counter == o2.counter) {
          return o1.object.compareTo(o2.object);
        } else {
          return o2.counter - o1.counter;
        }
      }
    };
  }
  
  public static <T extends Comparable<T>> Comparator<Counter<T>> getComparator() {
    return new Comparator<Counter<T>>() {
      @Override
      public int compare(Counter<T> o1, Counter<T> o2) {
        if (o1.counter == o2.counter) {
          return o1.object.compareTo(o2.object);
        } else {
          return o1.counter - o2.counter;
        }
      }
    };
  }
}

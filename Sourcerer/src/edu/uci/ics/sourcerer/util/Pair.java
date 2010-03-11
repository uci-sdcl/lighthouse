package edu.uci.ics.sourcerer.util;

import java.util.Iterator;

public class Pair <A,B>{
  private A a;
  private B b;
  
  public Pair(A a, B b) {
    this.a = a;
    this.b = b;
  }
  
  public A getA() {
    return a;
  }
  
  public B getB() {
    return b;
  }
  
  public static <A,B> Iterable<A> lhsIterable(final Iterable<Pair<A,B>> iterable) {
    return new Iterable<A>() {
      @Override
      public Iterator<A> iterator() {
        final Iterator<Pair<A,B>> iter = iterable.iterator();
        return new Iterator<A>() {
          @Override
          public void remove() {
            throw new UnsupportedOperationException("Removal not supported");
          }
        
          @Override
          public A next() {
            return iter.next().getA();
          }
        
          @Override
          public boolean hasNext() {
            return iter.hasNext();
          }
        };
      }
    };
  }
  
  public static <A,B> Iterable<B> rhsIterable(final Iterable<Pair<A,B>> iterable) {
    return new Iterable<B>() {
      @Override
      public Iterator<B> iterator() {
        final Iterator<Pair<A,B>> iter = iterable.iterator();
        return new Iterator<B>() {
          @Override
          public void remove() {
            throw new UnsupportedOperationException("Removal not supported");
          }
        
          @Override
          public B next() {
            return iter.next().getB();
          }
        
          @Override
          public boolean hasNext() {
            return iter.hasNext();
          }
        };
      }
    };
  }
}

/*
* Sourcerer: an infrastructure for large-scale source code analysis.
* Copyright (C) by contributors. See CONTRIBUTORS.txt for full list.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
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

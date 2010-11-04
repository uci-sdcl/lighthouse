package edu.uci.ics.sourcerer.util;

public class QuadTuple <A,B,C,D>{
  private A a;
  private B b;
  private C c;
  private D d;
  
  public QuadTuple(A a, B b, C c, D d) {
    this.a = a; 
    this.b = b;
    this.c = c;
    this.d = d;
  }

  public A getA() {
    return a;
  }

  public B getB() {
    return b;
  }

  public C getC() {
    return c;
  }

  public D getD() {
    return d;
  }
  
}

package com.test;

import java.util.Stack;

class MockQueue {

  public static int count(int i) {
    int t = 0, j;
    while (i > 0) {
      j = i % 2;
      if (j > 0)
        t++;
      i = i / 2;
    }
    return t;
  }

  private Stack<Object> a, b;

  public MockQueue() {
    a = new Stack<Object>();
    b = new Stack<Object>();
  }

  public void add(Object o) {
    a.add(o);
  }

  protected void move(Stack<Object> x, Stack<Object> y) {
    System.out.println("moving...");
    while (!x.empty())
      y.push(x.pop());
  }

  public Object remove() {
    if (b.size() == 0)
      move(a, b);
    return b.size() > 0 ? b.pop() : null;
  }

}

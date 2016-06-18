// This is supporting software for CS321/CS322 Compilers and Language Design.
// Copyright (c) Portland State University
//
// (For CS321 Lab7, A. Tolmach)
//

abstract class T {
  abstract int sum();
}

class T0 extends T {
  int sum() {
    return 0;
  }
}

class T1 extends T {
  private int x;
  private T left;
  private T right;
  T1 (int x, T left, T right) { 
      this.x = x; this.left = left; this.right = right; }

  int sum() {
    return x + left.sum() + right.sum();
  }
}

class T2 extends T{
  private int x1;
  private int x2;
  private T left;
  private T right;
  private T middle;
  T2 (int x1, int x2, T left, T middle, T right) { 
      this.x1 = x1; this.x2 = x2; this.left = left; this.middle = middle; this.right = right;  }
    

  int sum() {
    return x1 + x2 + left.sum() + right.sum() + middle.sum();
  }
}

class Example {
  public static void main (String argv[]) {
    T t = new T2 (3, 3, new T2(2, 2, new T0(), new T2(1,2, new T0(), new T0(), new T0()), new T0()),
                        new T2(4, 4, new T2(1, 1, new T0(), new T0(), new T0()), new T2(2, 2, new T0(), new T0(), new T0()), new T2(3, 3, new T0(), new T0(), new T0())), 
                     new T2(1, 1, new T0(), new T0(), new T0()));




    //T t = new T1 (1, new T1(2, new T0(), new T0()),
      //               new T1(4, new T0(), new T1(3, new T0(), new T0())));
    System.out.println ("sum = " + t.sum());
  }
}

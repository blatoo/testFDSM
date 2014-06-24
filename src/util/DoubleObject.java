package util;

public class DoubleObject<T> {
    private T object1;
    private T object2;
    public DoubleObject(T object1,T object2) {
      this.object1 = object1;
      this.object2 = object2;
    }
    public String toString() {
      return this.object1 + ", " + this.object2;
    }
    public static void main(String[] args) {
      DoubleObject<String> s = new DoubleObject<>("abc","def");
      DoubleObject<Integer> i = new DoubleObject<>(123,456);
      System.out.println("DoubleObject<String> s=" + s);
      System.out.println("DoubleObject<Integer> i=" + i);
    }
  }
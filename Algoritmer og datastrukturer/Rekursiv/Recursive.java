public class Recursive {

  //method one complexity is O(n)
  public static double methodOne (int n, double number) {
    if (n == 1) {
      return number;
    } else if (n > 1) {
      return number + methodOne(n - 1, number);
    }
    return 0;
  }

  //method two complexity is O(log n)
  public static double methodTwo (int n, double number) {
    if (n == 1) {
      return number;
    } else if (n % 2 == 0) {
      return methodTwo(n / 2, number + number);
    } else if (n % 2 != 0) {
      return number + methodTwo((n -1) / 2, number + number);
    }
    return 0;
  }

  public static void main(String[] args) {
   /* Test data to check the methods
    System.out.println(methodOne(13, 2.5));
    System.out.println(methodTwo(13, 2.5));

    System.out.println(methodOne(14,10.1));
    System.out.println(methodTwo(14,10.1));*/

  //different values of n
    int n1 = 5;
    int n2 = 10;
    int n3 = 20;
    int n4 = 100;
    int n5 = 1000;
    int n6 = 2000;
    int n7 = 4000;
    int n8 = 5000;

    double number = 140.2;
    

    long startTime1 = System.nanoTime();
    methodOne(n3, number);
    long endTime1 = System.nanoTime();
    long timeElapsed = endTime1 - startTime1;

    System.out.println("Time elapsed method one: " + timeElapsed);

    long startTime2 = System.nanoTime();
    methodTwo(n6, number);
    long endTime2 = System.nanoTime();
    long timeElapsed2 = endTime2 - startTime2;

    System.out.println("Time elapsed method two: " + timeElapsed2);
  }
}

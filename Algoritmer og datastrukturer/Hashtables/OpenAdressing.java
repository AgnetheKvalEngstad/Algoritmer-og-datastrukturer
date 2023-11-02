import java.util.Random;

class OpenAdressing {
  private int[] arr;
  private int kollisjon;
  private HashingMethod method;
  private static int[] randomNumbers;

  public enum HashingMethod {
    LINEAR_PROBING, DOUBLE_HASHING
  }

  public OpenAdressing(int length, HashingMethod method) {
    arr = new int[length];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = 0; // Assuming 0 to be a value that indicates empty slot
    }
    System.out.println("Array length: " + arr.length);
    this.kollisjon = 0;
    this.method = method;
  }

  public void push(int x) {
    if (method == HashingMethod.LINEAR_PROBING) {
      linearProbingInsert(x);
    } else {
      doubleHashingInsert(x);
    }
  }

  private void linearProbingInsert(int x) {
    int h1 = multiHash(x);
    while (arr[h1] != 0) {
      h1 = (h1 + 1) % arr.length; // Move to the next slot linearly
      kollisjon++;
    }
    arr[h1] = x;
  }

  private void doubleHashingInsert(int x) {
    int h1 = multiHash(x);
    if (arr[h1] == 0) {
      arr[h1] = x;
    } else {
      int counter = 1;
      while (counter < arr.length) {
        int h2 = Math.abs((modHash(x) * counter + h1) % arr.length); // Modified to use arr.length
        if (arr[h2] == 0) {
          arr[h2] = x;
          break;
        } else {
          counter++;
          kollisjon++;
        }
      }
    }
  }

  public int multiHash(int k) {
    double A = k* ((Math.sqrt(5.0)-1.0)/2.0);
    A -= (int) A;
    return (int) (arr.length * Math.abs(A));
  }

  public int modHash(int k) {
    return ((2*Math.abs(k) + 1) % arr.length);
  }

  public static int[] generateRandomNumbers(int m) {
    randomNumbers = new int[m];

    // Initialize the first number as 1
    randomNumbers[0] = 1;

    Random random = new Random();

    // Generate random numbers for the rest of the array
    for (int i = 1; i < m; i++) {
      // Generate a random number between 1 and 1000
      int randomIncrement = random.nextInt(1000) + 1;

      // Set the next number as the previous number plus the random increment
      randomNumbers[i] = randomNumbers[i - 1] + randomIncrement;
    }

    // Shuffle the array using Fisher-Yates shuffle
    for (int i = m - 1; i > 0; i--) {
      int j = random.nextInt(i + 1);
      int temp = randomNumbers[i];
      randomNumbers[i] = randomNumbers[j];
      randomNumbers[j] = temp;
    }

    return randomNumbers;
  }

  public static void conductExperiment(OpenAdressing ht, int range, double percentage, String method) {
    long start, end, tot = 0;
    int insertCount = (int) (ht.arr.length * percentage);

    for (int i = 0; i < insertCount; i++) {
      int random = randomNumbers[i]; // Hent tall fra arrayet
      start = System.nanoTime();
      ht.push(random);
      end = System.nanoTime();
      tot += end - start;
    }

    System.out.println("[" + method + " | " + (percentage * 100) + "%] Time to fill table: " + tot / 1000000 + "ms");
    System.out.println("[" + method + " | " + (percentage * 100) + "%] Collisions: " + ht.kollisjon);
    System.out.println("-----------------------------");
  }

  public static void main(String[] args) {
    int range = 10000019;
    generateRandomNumbers(range);

    double[] percentages = {0.50, 0.80, 0.90, 0.99, 1.0}; // 50%, 80%, 90%, 99%, 100%

    for (double percentage : percentages) {
      System.out.println("Conducting experiment for " + (percentage * 100) + "% insertion...");

      OpenAdressing linearHashTable = new OpenAdressing(range, HashingMethod.LINEAR_PROBING);
      conductExperiment(linearHashTable, range, percentage, "Linear Probing");

      OpenAdressing doubleHashingHashTable = new OpenAdressing(range, HashingMethod.DOUBLE_HASHING);
      conductExperiment(doubleHashingHashTable, range, percentage, "Double Hashing");
    }
  }
}

import java.util.Random;
public class Profit {

  public static String stockCalculator(int[] list) {
    int profit;
    int currentProfit = 0;
    int length = list.length;
    int buy = -1;
    int sell = -1;

    for (int n = 0; n < length; n++) { //The loop that creates the O(n^2) complexity
      profit = 0;
      for (int m = n; m < length; m++) {
        profit += list[m];
        if (profit > currentProfit) {
          currentProfit = profit;
          buy = n;
          sell = m + 1;
        }
      }
    }

    return "Profit: " + currentProfit + ", Buy day Nr: " + buy + ", Sell day Nr: " + sell;
  }

  public static void main(String[] args) {

    int[] exList = {-1, 3, -9, 2, 2, -1, 2, -1, -5}; //Example list
    int[] randomList = new int[40000]; //Random list where the length input is adjusted
    Random rand = new Random();
    for (int i = 0; i < randomList.length; i++) {
      randomList[i] = rand.nextInt(-5, 10);
    }

    String output;
    long start = System.nanoTime();
    output = stockCalculator(randomList);

    long stop = System.nanoTime();
    long time = (stop - start) / 1000000;
    System.out.println("Example list: " + stockCalculator(exList) + "\n" + "Random list: "
        + output + "\n" + "Runtime: " + time + " milliseconds ");

  }
}

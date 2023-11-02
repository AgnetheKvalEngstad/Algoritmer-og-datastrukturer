import java.util.Random;

public class oving_3 {

  public static int runBubbleThreshold = 14750; // Terskelverdi for å bytte til boblesortering
  public static int arraySize = 1000000; // Størrelse på arrayen

  public static void main(String[] args) {
    // Generer en array og mål tiden det tar å sortere den
    int[] array = generateArray(arraySize);
    int [] arrayCopy = array.clone();

    System.out.println(sumArray(array));

    long startTime = System.nanoTime();
    quickSort(array, 0, array.length - 1);
    long endTime = System.nanoTime();
    long elapsedTime = endTime - startTime;

    long startTime2 = System.nanoTime();
    quickSortUten(arrayCopy, 0, arrayCopy.length - 1);
    long endTime2 = System.nanoTime();
    long elapsedTime2 = endTime2 - startTime2;

    //sortere allerede sortert tabell for å se tiden
    long startTime3 = System.nanoTime();
    quickSort(array, 0, array.length - 1);
    long endTime3 = System.nanoTime();
    long elapsedTime3 = endTime3 - startTime3;

    System.out.println(sumArray(array));
    System.out.println(testSorted(array));
    System.out.println("quickSort med hjelpealgoritme bruker: " + (elapsedTime / 1000000) + " ms");
    System.out.println("quickSort med hjelpealgoritme og ferdig sortert liste bruker: " + (elapsedTime3 / 1000000) + " ms");
    System.out.println("quickSort uten hjelpealgoritme bruker: " + (elapsedTime2 / 1000000) + " ms");
  }

//  /**
//   * Sjekker om arrayen er delvis sortert
//   * @param array Arrayen som sjekkes
//   * @return true hvis den er delvis sortert
//   */
//  public static boolean isPartiallySorted(int[] array) {
//    for (int i = 0; i < array.length - 1; i++) {
//      if (array[i] > array[i + 1]) {
//        return false;
//      }
//    }
//    return true;
//  }

  /**
   * Implementerer quicksort-algoritmen med rekursjon og bytte til boblesortering ved lav deltabellstørrelse.
   *
   * @param array Arrayen som skal sorteres.
   * @param low   Laveste indeks i deltabellen.
   * @param high  Høyeste indeks i deltabellen.
   */
  public static void quickSort(int[] array, int low, int high) {
    if (high - low > runBubbleThreshold) {
      int pivotIndex = partition(array, low, high);
      quickSort(array, low, pivotIndex - 1);
      quickSort(array, pivotIndex + 1, high);
    } else {
      bubbleSort(array, low, high);
    }
  }

  /**
   * Implementerer quicksort-algoritmen med rekursjon.
   *
   * @param array Arrayen som skal sorteres.
   * @param low Laveste indeks i deltabellen.
   * @param high Høyeste indeks i deltabellen.
   */
  public static void quickSortUten(int[] array, int low, int high) {
    int pivotIndex = partition(array, low, high);
    quickSort(array, low, pivotIndex - 1);
    quickSort(array, pivotIndex + 1, high);
  }

  /**
   * Utfører partisjonering av deltabellen for quicksort.
   *
   * @param array Arrayen som skal sorteres.
   * @param low   Laveste indeks i deltabellen.
   * @param high  Høyeste indeks i deltabellen.
   * @return Indeks for pivot-elementet etter partisjonering.
   */
  private static int partition(int[] array, int low, int high) {
    int leftIndex, rightIndex;
    int medianIndex = median3Sort(array, low, high);
    int pivotValue = array[medianIndex];
    swap(array, medianIndex, high - 1);
    for (leftIndex = low, rightIndex = high - 1; ; ) {
      while (array[++leftIndex] < pivotValue) ;
      while (array[--rightIndex] > pivotValue) ;
      if (leftIndex >= rightIndex) break;
      swap(array, leftIndex, rightIndex);
    }
    swap(array, leftIndex, high - 1);
    return leftIndex;
  }

  /**
   * Utfører median-3 sortering for å finne et godt pivot-element.
   *
   * @param array Arrayen som skal sorteres.
   * @param low   Laveste indeks i deltabellen.
   * @param high  Høyeste indeks i deltabellen.
   * @return Indeks for pivot-elementet.
   */
  private static int median3Sort(int[] array, int low, int high) {
    int middle = (low + high) / 2;
    if (array[low] > array[middle]) swap(array, low, middle);
    if (array[middle] > array[high]) {
      swap(array, middle, high);
      if (array[low] > array[middle]) swap(array, low, middle);
    }
    return middle;
  }

  /**
   * Utfører boblesortering på en deltabell.
   *
   * @param array Arrayen som skal sorteres.
   * @param low   Laveste indeks i deltabellen.
   * @param high  Høyeste indeks i deltabellen.
   */
  private static void bubbleSort(int[] array, int low, int high) {
    for (int i = high; i >= low; --i) {
      for (int j = low; j < i; ++j) {
        if (array[j] > array[j + 1]) {
          swap(array, j, j + 1);
        }
      }
    }
  }

  /**
   * Bytter to elementer i en array.
   *
   * @param array   Arrayen som inneholder elementene.
   * @param index1  Indeks for det første elementet.
   * @param index2  Indeks for det andre elementet.
   */
  private static void swap(int[] array, int index1, int index2) {
    int temp = array[index1];
    array[index1] = array[index2];
    array[index2] = temp;
  }

  /**
   * Genererer en tilfeldig array med gitt størrelse.
   *
   * @param size Størrelsen på arrayen.
   * @return Den genererte arrayen.
   */
  private static int[] generateArray(int size) {
    int[] array = new int[size];
    Random random = new Random();
    for (int i = 0; i < array.length; i++) {
      array[i] = random.nextInt(100);
    }
    return array;
  }

  /**
   * Tester om en array er sortert i stigende rekkefølge.
   *
   * @param array Arrayen som skal testes.
   * @return En melding som indikerer om arrayen er sortert eller ikke.
   */
  private static String testSorted(int[] array) {
    for (int i = 0; i < array.length - 1; i++) {
      if (array[i] > array[i + 1]) {
        return "The array is not sorted";
      }
    }
    return "The array is sorted";
  }

  /**
   * Beregner summen av elementene i en array.
   *
   * @param array Arrayen som skal summeres.
   * @return Summen av elementene i arrayen.
   */
  public static int sumArray(int[] array) {
    int sum = 0;
    for (int i = 0; i < array.length; i++) {
      sum += array[i];
    }
    return sum;
  }
}

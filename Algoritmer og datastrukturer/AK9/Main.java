import java.io.*;
import java.util.*;

class Main {

  public static void main(String[] args) throws IOException {
    Scanner input = new Scanner(System.in);

    System.out.println("Node fil: ");
    String fileVert = input.nextLine();

    System.out.println("Kant file: (Vennligst vent etter å ha trykket enter, dette kan ta noen sekunder)");
    String fileEdge = input.nextLine();

    BufferedReader readerVert =
        new BufferedReader(new InputStreamReader(
            Objects.requireNonNull(Main.class.getResourceAsStream(fileVert))));
    BufferedReader readerEdge =
        new BufferedReader(new InputStreamReader(
            Objects.requireNonNull(Main.class.getResourceAsStream(fileEdge))));

    Graph graph = new Graph();
    graph.newGraph(readerVert, readerEdge);
    readerVert.close();
    readerEdge.close();

    while (true) {
      System.out.println("1. Finn vei fra a til b");
      System.out.println("2. Find de nærmeste 5 landemerkene");
      System.out.println("3. Preprosessering for ALT alogritmen");

      int choice = input.nextInt();
      input.nextLine();

      switch (choice) {
        case 1 -> {
          System.out.println("Fra sted(node id): ");
          int locationFrom = input.nextInt();
          System.out.println("Til sted(node id): ");
          int locationTo = input.nextInt();
          System.out.println("1. Dijkstra");
          System.out.println("2. ALT");
          int type = input.nextInt();

          if (type == 1) {

            //If user chooses Dijkstra:
            long startTime = System.nanoTime();
            int nodesPicked = graph.dijkstra(locationFrom);
            long endTime = System.nanoTime();
            double time = (((double) endTime - (double) startTime) / (double) 1000000000);
            System.out.println(graph.getCoordinatesFromPath(locationTo));
            int pathTime = ((Previous) graph.vertices[locationTo].nodeData).distance / 100;
            int hours = pathTime / 3600;
            int minutes = (pathTime % 3600) / 60;
            int seconds = pathTime % 60;
            System.out.println("""
              Ruten tar (hh:mm:ss): %02d:%02d:%02d
              Algoritme tid: %.2f sekunder
              Noder plukket fra kø: %d
            """.formatted(hours, minutes, seconds, time, nodesPicked));

          } else if (type == 2) {

            //If user chooses ALT: input.nextLine();

            System.out.println("Vennligst fyll inn 'fra landemerke' fil: ");
            input.nextLine();
            String fromLand = input.nextLine();
            BufferedReader readerfromLand =
                new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(Main.class.getResourceAsStream(fromLand))));
            StringTokenizer fromSt = new StringTokenizer(readerfromLand.readLine());
            int numVertex = Integer.parseInt(fromSt.nextToken());
            int numLands = Integer.parseInt(fromSt.nextToken());
            int[][] fromTable = new int[numVertex][numLands];

            for (int i = 0; i < numVertex; i++) {
              fromSt = new StringTokenizer(readerfromLand.readLine());
              for (int j = 0; j < numLands; j++) {
                int dist = Integer.parseInt(fromSt.nextToken());
                fromTable[i][j] = dist;
              }
            }

            System.out.println("Vennligst fyll inn 'til landemerke' fil: ");
            String toLand = input.nextLine();
            BufferedReader readerToLand =
                new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(Main.class.getResourceAsStream(toLand))));
            StringTokenizer toSt = new StringTokenizer(readerToLand.readLine());
            int numVertex2 = Integer.parseInt(toSt.nextToken());
            int numLands2 = Integer.parseInt(toSt.nextToken());
            int[][] toTable = new int[numVertex2][numLands2];

            for (int i = 0; i < numVertex2; i++) {
              toSt = new StringTokenizer(readerToLand.readLine());
              for (int j = 0; j < numLands2; j++) {
                int dist = Integer.parseInt(toSt.nextToken());
                toTable[i][j] = dist;
              }
            }
            long startTime = System.nanoTime();
            int nodesPicked = graph.ALT(fromTable, toTable, locationFrom, locationTo);
            long endTime = System.nanoTime();
            double time = (((double) endTime - (double) startTime) / (double) 1000000000);
            System.out.println(graph.getCoordinatesFromPath(locationTo));
            int pathTime = ((Previous) graph.vertices[locationTo].nodeData).distance / 100;
            int hours = pathTime / 3600;
            int minutes = (pathTime % 3600) / 60;
            int seconds = pathTime % 60;
            System.out.println("""
              Ruten tar (hh:mm:ss): %02d:%02d:%02d
              Algoritme tid: %.2f sekunder
              Noder plukket fra kø: %d
            """.formatted(hours, minutes, seconds, time, nodesPicked));


          }
        }
        case 2 -> {

          //Closest 5 landmarks to a location:
          System.out.println("landemerkefil: ");
          String fileLand = input.nextLine();
          BufferedReader readerLand =
              new BufferedReader(new InputStreamReader(
                  Objects.requireNonNull(Main.class.getResourceAsStream(fileLand))));
          System.out.println("Lokasjon(node id): ");
          int loc = input.nextInt();
          System.out.println(""" 
              1 Stedsnavn
               
              2 Bensinstasjon
               
              4 Ladestasjon
               
              8 Spisested
                                                   
              16 Drikkested
                                                   
              32 Overnattingssted 
              """);
          int landType = input.nextInt();
          long startTime = System.nanoTime();
          Vertex[] rest = graph.nearest5(loc, landType, readerLand);
          long endTime = System.nanoTime();
          double time = (((double) endTime - (double) startTime) / (double) 1000000000);
          System.out.println("Algoritme tid: " + time + " sekunder");
          for (Vertex v : rest) {
            System.out.println(v.latitude + ", " + v.longitude + "\n");
          }
        }
        case 3 -> {

          //Preprocess for ALT:
          //Kongsberg, Lillehammer, Røros

          int[] landmarksId = {1945152,6627812,3166030};
          int[][] fromLandmarkTable = graph.landmarkTable(landmarksId);
          BufferedReader readerVert2 =
              new BufferedReader(new InputStreamReader(
                  Objects.requireNonNull(Main.class.getResourceAsStream(fileVert))));
          BufferedReader readerEdge2 =
              new BufferedReader(new InputStreamReader(
                  Objects.requireNonNull(Main.class.getResourceAsStream(fileEdge))));

          File fromLandmarksFile = File.createTempFile("fraLandemerke", ".txt", new File(System.getProperty("user.dir")));
          BufferedWriter buffer = new BufferedWriter(new FileWriter(fromLandmarksFile));
          buffer.write(graph.vertices.length + " " + landmarksId.length);
          buffer.newLine();
          System.out.println(fromLandmarkTable.length);
          for (int[] ints : fromLandmarkTable) {
            for (int j = 0; j < landmarksId.length; j++) {
              buffer.write(ints[j] + " ");
            }
            buffer.newLine();
          }


          buffer.close();

          graph = new Graph();
          graph.reverseGraph(readerVert2, readerEdge2);
          int[][] toLandmarkTable = graph.landmarkTable(landmarksId);
          File toLandmarksFile = File.createTempFile("tilLandemerke", ".txt", new File(System.getProperty("user.dir")));
          BufferedWriter buffer2 = new BufferedWriter(new FileWriter(toLandmarksFile));
          buffer2.write(graph.vertices.length + " " + landmarksId.length);
          buffer2.newLine();
          for (int i = 0; i < toLandmarkTable.length; i++) {
            for (int j = 0; j < landmarksId.length; j++) {
              buffer2.write(toLandmarkTable[i][j] + " ");
            }
            buffer2.newLine();
          }
          buffer2.close();
        }
      }
    }
  }
}


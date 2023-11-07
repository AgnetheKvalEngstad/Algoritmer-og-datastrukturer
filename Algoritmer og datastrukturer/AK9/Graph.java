import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;


class Graph {

  int numberOfVertex,
      numberOfEdge;
  Vertex[] vertices;


  public void newGraph(BufferedReader vertBr, BufferedReader edgeBr) throws IOException {
    StringTokenizer vertSt = new StringTokenizer(vertBr.readLine());
    numberOfVertex = Integer.parseInt(vertSt.nextToken());
    // creates a new graph from the given files.
    vertices = new Vertex[numberOfVertex];

    for (int i = 0; i < numberOfVertex; ++i) {
      vertSt = new StringTokenizer(vertBr.readLine());

      int id = Integer.parseInt(vertSt.nextToken());
      double latitude = Double.parseDouble(vertSt.nextToken());
      double longitude = Double.parseDouble(vertSt.nextToken());
      vertices[i] = new Vertex(id, latitude, longitude);
    }

    initNodeDate(vertices);

    StringTokenizer edgeSt = new StringTokenizer(edgeBr.readLine());
    numberOfEdge = Integer.parseInt(edgeSt.nextToken());

    for (int i = 0; i < numberOfEdge; ++i) {
      edgeSt = new StringTokenizer(edgeBr.readLine());

      int fromNode = Integer.parseInt(edgeSt.nextToken());
      int toNode = Integer.parseInt(edgeSt.nextToken());
      int weight = Integer.parseInt(edgeSt.nextToken());
      ((Previous) vertices[fromNode].nodeData).distance = fromNode;

      vertices[fromNode].edge = new WeightedEdge(vertices[toNode], vertices[fromNode].edge, weight);
    }
  }


  public void reverseGraph(BufferedReader vertBr, BufferedReader edgeBr) throws IOException {
    StringTokenizer vertexString = new StringTokenizer(vertBr.readLine());
    numberOfVertex = Integer.parseInt(vertexString.nextToken());
    vertices = new Vertex[numberOfVertex];

    for (int i = 0; i < numberOfVertex; ++i) {
      vertexString = new StringTokenizer(vertBr.readLine());

      int id = Integer.parseInt(vertexString.nextToken());
      double latitude = Double.parseDouble(vertexString.nextToken());
      double longitude = Double.parseDouble(vertexString.nextToken());
      vertices[i] = new Vertex(id, latitude, longitude);
    }

    initNodeDate(vertices);

    StringTokenizer edgeString = new StringTokenizer(edgeBr.readLine());
    numberOfEdge = Integer.parseInt(edgeString.nextToken());

    for (int i = 0; i < numberOfEdge; ++i) {
      edgeString = new StringTokenizer(edgeBr.readLine());

      int toNode = Integer.parseInt(edgeString.nextToken());
      int fromNode = Integer.parseInt(edgeString.nextToken());
      int weight = Integer.parseInt(edgeString.nextToken());
      ((Previous) vertices[fromNode].nodeData).distance = fromNode;

      vertices[fromNode].edge = new WeightedEdge(vertices[toNode], vertices[fromNode].edge, weight);
    }
  }


  public Vertex[] nearest5(int location, int type, BufferedReader reader) throws IOException {
    Map<Integer, Integer> landmarks = new HashMap<>();
    Vertex[] closest = new Vertex[5];

    Comparator<Vertex> Comp = Comparator.comparingInt(o -> ((Previous) o.nodeData).distance);
    TreeSet<Vertex> set = new TreeSet<>(Comp);

    StringTokenizer st = new StringTokenizer(reader.readLine());
    numberOfVertex = Integer.parseInt(st.nextToken());

    for (int i = 0; i < numberOfVertex; ++i) {
      st = new StringTokenizer(reader.readLine());

      int id = Integer.parseInt(st.nextToken());
      int code = Integer.parseInt(st.nextToken());
      landmarks.put(id, code);
    }

    dijkstra(location);

    for (Map.Entry<Integer, Integer> code : landmarks.entrySet()) {
      if ((code.getValue() & type) == type) {
        Vertex match = vertices[code.getKey()];
        set.add(match);
      }

    }

    for (int i = 0; i < 5; i++) {
      closest[i] = set.pollFirst();
    }
    return closest;

  }


  public void initNodeDate(Vertex[] vertices) {
    for (Vertex vertex : vertices) {
      vertex.nodeData = new Previous();
    }
  }


  public int dijkstra(int id) {
    int count = 0;
    initPrev(vertices[id]);
    PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(10,
        Comparator.comparingInt(o -> ((Previous) o.nodeData).distance));
    priorityQueue.add(vertices[id]);
    for (int i = numberOfVertex; i > 1; i--) {
      if (priorityQueue.isEmpty()) break;
      Vertex vertex = priorityQueue.poll();
      count++;
      for (WeightedEdge edge = (WeightedEdge) vertex.edge; edge != null; edge = (WeightedEdge) edge.next) {
        boolean opt = optimizeDistance(vertex, edge);
        if (opt) {
          priorityQueue.remove(edge.to);
          priorityQueue.add(edge.to);
        }
      }
    }
    return count;
  }


  public int ALT(int[][] fromLandmark, int[][] toLandmark, int startNodeId, int endNodeId) {
    int nodesPicked = 0;
    boolean reachedGoal = false;

    initPrev(vertices[startNodeId]);
    PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(10,
        Comparator.comparingInt(vertex ->
            ((Previous) vertex.nodeData).distance + ((Previous) vertex.nodeData).distanceFromGoal));

    priorityQueue.add(vertices[startNodeId]);

    while (!reachedGoal) {
      if (priorityQueue.isEmpty()) break;
      Vertex vertex = priorityQueue.poll();
//            System.out.println(vertex.latitude + ", " + vertex.longitude);
      nodesPicked++;
      if (vertex.id == endNodeId) break;

      for (WeightedEdge edge = (WeightedEdge) vertex.edge; edge != null; edge = (WeightedEdge) edge.next) {

        if (((Previous) edge.to.nodeData).distanceFromGoal == Previous.infinity) { //Is just set one time for each node.
          estimateDistanceFromGoal(fromLandmark, toLandmark, edge.to, endNodeId);
          priorityQueue.add(edge.to);
        }
        boolean opt = optimizeDistance(vertex, edge);
        if (opt) {
          priorityQueue.remove(edge.to);
          priorityQueue.add(edge.to);
        }

      }
    }

    return nodesPicked;
  }

  private void estimateDistanceFromGoal(int[][] fromLandmark, int[][] toLandmark, Vertex vertex, int goalNodeNr) {
    int totalBestEst = 0;
    for (int i = 0; i < fromLandmark[0].length; i++) {
      int landmarkEstimate = fromLandmark[goalNodeNr][i] - fromLandmark[vertex.id][i];
      if (landmarkEstimate < 0) landmarkEstimate = 0;
      int landmarkEstimate2 = toLandmark[vertex.id][i] - toLandmark[goalNodeNr][i];
      if (landmarkEstimate2 < 0) landmarkEstimate2 = 0;

      int landmarkBestEstimate = Math.max(landmarkEstimate, landmarkEstimate2);
      totalBestEst = Math.max(totalBestEst, landmarkBestEstimate);
    }
    ((Previous) vertex.nodeData).distanceFromGoal = totalBestEst;
  }


  public int[][] landmarkTable(int[] landmarksId) {
    int[][] table = new int[vertices.length][landmarksId.length];

    for (int i = 0; i < landmarksId.length; i++) {
      dijkstra(landmarksId[i]);
      for (int j = 0; j < vertices.length; j++) {
        table[j][i] = ((Previous) vertices[j].nodeData).distance;
      }
    }
    return table;
  }

  public boolean optimizeDistance(Vertex vertex, WeightedEdge edge) {
    Previous node = (Previous) vertex.nodeData;
    Previous nextNode = (Previous) edge.to.nodeData;
    int newDist = node.distance + edge.weight;
    if (nextNode.distance > newDist) {
      nextNode.distance = newDist;
      nextNode.previous = vertex;
      return true;
    }
    return false;
  }


  public String getCoordinatesFromPath(int id) {
    Vertex startNode = vertices[id];
    StringBuilder sb = new StringBuilder();

    int count = 0;
    while (((Previous) startNode.nodeData).previous != null) {
      startNode = ((Previous) startNode.nodeData).previous;
      if (count % 4 == 0) {
        sb.append(startNode.latitude).append(", ").append(startNode.longitude).append("\n");
      }
      count++;
    }

    return sb.toString();
  }


  public void initPrev(Vertex rootVertex) {
    for (Vertex vertex : vertices) {
      vertex.nodeData = new Previous();
    }
    ((Previous) rootVertex.nodeData).distance = 0;
  }
}
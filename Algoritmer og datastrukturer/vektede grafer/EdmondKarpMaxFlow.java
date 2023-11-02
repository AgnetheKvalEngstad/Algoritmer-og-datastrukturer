import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class EdmondKarpMaxFlow {

  public class Edge {
    Edge reverseFlow;
    final int capacity;
    int to, from;
    int edgeFlow;


    /**
     * Instantiates a new Edge.
     *
     * @param to     the to
     * @param from   the from
     * @param weight the weight
     */
    public Edge(int to, int from, int weight) {
      this.to = to;
      this.from = from;
      this.capacity = weight;
    }

    public int getCapacity() {
      return capacity - edgeFlow;
    }

    /**
     * Change flow.
     *
     * @param minimalCapacity the minimal capacity
     */
    public void changeFlow(int minimalCapacity) {
      edgeFlow += minimalCapacity;
      reverseFlow.edgeFlow -= minimalCapacity;
    }
  }

  private int n, k;
  private List<List<Edge>> graphTable;

  public EdmondKarpMaxFlow(BufferedReader br) throws IOException {
    graphReader(br);
  }

  /**
   * Interprets the graph.
   *
   * @param br the br
   * @throws IOException the io exception
   */
  public void graphReader(BufferedReader br) throws IOException {
    StringTokenizer st = new StringTokenizer(br.readLine());
    n = Integer.parseInt(st.nextToken());
    graphTable = new ArrayList<>(n);

    for (int i = 0; i < this.n; i++) {
      graphTable.add(new ArrayList<>());
    }
    k = Integer.parseInt(st.nextToken());

    for (int i = 0; i < k; i++) {
      st = new StringTokenizer(br.readLine());
      int from = Integer.parseInt(st.nextToken());
      int to = Integer.parseInt(st.nextToken());
      int weight = Integer.parseInt(st.nextToken());
      addEdge(from, to, weight);
    }
  }

  public void addEdge(int vertexFrom, int vertexTo, int weight) {
    if (vertexFrom <= n) {
      Edge edge1 = new Edge(vertexTo, vertexFrom, weight);
      Edge edge2 = new Edge(vertexFrom, vertexTo, 0);
      graphTable.get(vertexFrom).add(edge1);
      graphTable.get(vertexTo).add(edge2);
      edge1.reverseFlow = edge2;
      edge2.reverseFlow = edge1;
    }
  }


  /**
   * Bfs.
   *
   * @param start the start
   * @param sink  the sink
   * @return the int
   */
  public int bfs(int start, int sink) {
    boolean[] visited = new boolean[n];
    Integer[] prevNode = new Integer[n];
    int j = 0;
    Queue<Integer> q = new ArrayDeque<>(n);
    visited[start] = true;
    q.offer(start);

    Edge[] prevEdge = new Edge[n];
    while (!q.isEmpty()) {
      int node = q.poll();
      if (node == sink) break;

      for (Edge edge : graphTable.get(node)) {
        int cap = edge.getCapacity();
        if (cap > 0 && !visited[edge.to]) {
          visited[edge.to] = true;
          prevNode[edge.to] = node;
          prevEdge[edge.to] = edge;
          q.offer(edge.to);
        }
      }
    }

    if (prevEdge[sink] == null) return 0;

    int flow = Integer.MAX_VALUE;
    for (Edge edge = prevEdge[sink]; edge != null; edge = prevEdge[edge.from])
      flow = Math.min(flow, edge.getCapacity());

    for (Edge edge = prevEdge[sink]; edge != null; edge = prevEdge[edge.from]) edge.changeFlow(flow);

    String str = "";
    for (int i = sink; i != start; i = prevNode[i]){
      str = i + " " + str + " ";
    }
    System.out.println(flow + "        " + start + " " + str);

    return flow;
  }

  /**
   * Edmond karp.
   *
   * @param start the start
   * @param sink  the sink
   */
  public void edmondKarp(int start, int sink){
    int maxFlow = 0;
    int flow;
    do {
      flow = bfs(start, sink);
      maxFlow += flow;
    } while (flow != 0);
    System.out.println("Max flow " + maxFlow);
  }


  public static void main(String[] args) throws IOException {
    EdmondKarpMaxFlow graph;
    String fileName = "flytgraf3";
    FileReader file = new FileReader(fileName);
    BufferedReader br = new BufferedReader(file);
    graph = new EdmondKarpMaxFlow(br);

    System.out.printf("Running Edmond Karp on: " + fileName);
    System.out.println("\nMaximum flow graph from start K to sink S with Edmond Karp: ");
    System.out.println("Augmentation : Augmenting path");
    graph.edmondKarp(0, 1);
  }
}

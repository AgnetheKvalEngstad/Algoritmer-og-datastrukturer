import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class UvektedeGrafer {
  private int numberOfNodes;
  private List<List<Integer>> graph;
  private boolean[] visited;
  private int[] distance;
  private int[] predecessor;

  public UvektedeGrafer(int numberOfNodes) {
    this.numberOfNodes = numberOfNodes;
    graph = new ArrayList<>(numberOfNodes);
    for (int i = 0; i < numberOfNodes; i++) {
      graph.add(new ArrayList<>());
    }
    visited = new boolean[numberOfNodes];
    distance = new int[numberOfNodes];
    predecessor = new int[numberOfNodes];
  }

  public void addEdge(int from, int to) {
    graph.get(from).add(to);
  }

  public void bfs(int startNode) {
    Queue<Integer> queue = new LinkedList<>();
    List<Integer> sortedNodes = new ArrayList<>();
    visited[startNode] = true;
    distance[startNode] = 0;
    predecessor[startNode] = -1;
    queue.add(startNode);

    while (!queue.isEmpty()) {
      int currentNode = queue.poll();
      sortedNodes.add(currentNode);

      List<Integer> neighbors = graph.get(currentNode);
      for (Integer neighbor : neighbors) {
        if (!visited[neighbor]) {
          visited[neighbor] = true;
          distance[neighbor] = distance[currentNode] + 1;
          predecessor[neighbor] = currentNode;
          queue.add(neighbor);
        }
      }
    }
    Collections.sort(sortedNodes);
    for (Integer node : sortedNodes) {
      System.out.println("Node: " + node +
          " Forgjenger: " + predecessor[node] +
          " Dist: " + distance[node]);
    }
  }

  public void topologicalSort() {
    Stack<Integer> stack = new Stack<>();
    boolean[] visited = new boolean[numberOfNodes];

    for (int i = 0; i < numberOfNodes; i++) {
      if (!visited[i]) {
        dfs(i, visited, stack);
      }
    }

    System.out.println("Topological Sort Order:");
    while (!stack.isEmpty()) {
      System.out.print(stack.pop() + " ");
    }
  }



  private void dfs(int node, boolean[] visited, Stack<Integer> stack) {
    visited[node] = true;
    List<Integer> neighbors = graph.get(node);

    for (Integer neighbor : neighbors) {
      if (!visited[neighbor]) {
        dfs(neighbor, visited, stack);
      }
    }

    stack.push(node);
  }

  public static void main(String[] args) throws IOException {
    FileReader fileReader = new FileReader("src/ø6g1.txt");
    BufferedReader bufferedReader = new BufferedReader(fileReader);


    StringTokenizer tokenizer = new StringTokenizer(bufferedReader.readLine());
    int numberOfNodes = Integer.parseInt(tokenizer.nextToken());
    int numberOfEdges = Integer.parseInt(tokenizer.nextToken());

    UvektedeGrafer ug = new UvektedeGrafer(numberOfNodes);


    for (int i = 0; i < numberOfEdges; i++) {
      tokenizer = new StringTokenizer(bufferedReader.readLine());
      int from = Integer.parseInt(tokenizer.nextToken());
      int to = Integer.parseInt(tokenizer.nextToken());
      ug.addEdge(from, to);
    }

    bufferedReader.close();

    // Start BFS fra en bestemt startnode
    int startNode = 5;
    System.out.println("BFS starting from node " + startNode + ":");
    ug.bfs(startNode);

    // Utfører topologisk sortering
    System.out.println("\nTopological Sort:");
    ug.topologicalSort();
  }
}

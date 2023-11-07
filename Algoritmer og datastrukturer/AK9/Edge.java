class Edge {
  Edge next;
  Vertex to;
  public Edge(Vertex v, Edge nxt){
    to = v;
    next = nxt;
  }
}
class WeightedEdge extends Edge {
  int weight;

  public WeightedEdge(Vertex v, Edge next, int weight) {
    super(v, next);
    this.weight = weight;
  }
}
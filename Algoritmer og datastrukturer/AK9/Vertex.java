class Vertex {
  Edge edge;
  int id;
  double latitude;
  double longitude;
  Object nodeData;
  public Vertex(int id, double latitude, double longitude){
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
class Previous{
  int distance;
  int distanceFromGoal;
  Vertex previous;
  static int infinity = Integer.MAX_VALUE;
  public int getDistance(){return distance;}
  public Vertex getPrevious(){return previous;}

  public Previous(){
    distance = infinity;
    distanceFromGoal = infinity;
  }
}
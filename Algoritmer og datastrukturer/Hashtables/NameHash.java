public class NameHash {
  private String name;
  private NameHash next;

  public NameHash(String name){
    this.name = name;
  }

  public void setNext(NameHash next){
    this.next = next;
  }
  public NameHash getNext(){
    return next;
  }

  public String getName(){
    return name;
  }

  public String toString(){
    return name;
  }

}

import java.util.PriorityQueue;

public class Node{
  char letter;
  String [] bitString;
  Node left;
  Node right;
  int symbols;


  public Node(char character, int i, Node left, Node right){
    this.letter = character;
    bitString = new String[256];
    this.left = left;
    this.right = right;
    this.symbols = i;

  }
  public Node(){
    bitString = new String[256];
  }

  private static int nodeSum(Node node1, Node node2){
    return node1.symbols + node2.symbols;
  }

  public boolean isLeafNode(){
    return this.left == null && this.right == null;
  }

  public static Node huffmanTree(PriorityQueue<Node> heap){
    Node tree = new Node();

    while(heap.size() > 1){
      Node first = heap.poll();
      Node second = heap.poll();
      Node end = new Node('\0',nodeSum(first, second), second, first);
      heap.add(end);
      tree = end;
    }
    return tree;

  }

  public void printOut(Node node, String bitString) {
    if (!node.isLeafNode()) {

      printOut(node.left, bitString + "0");
      printOut(node.right, bitString + "1");

    }
    else {
      this.bitString[node.letter] = bitString;
    }

  }
}



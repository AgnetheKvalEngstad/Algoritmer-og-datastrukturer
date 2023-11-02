class Node {
  int data;
  Node next;

  public Node(int data) {
    this.data = data;
    this.next = null;
  }
}

class CircularLinkedList {
  private Node head;

  public void insert(int data) {
    Node newNode = new Node(data);
    if (head == null) {
      head = newNode;
      head.next = head; // Point to itself for circular behavior
    } else {
      Node current = head;
      while (current.next != head) {
        current = current.next;
      }
      current.next = newNode;
      newNode.next = head;
    }
  }

  public void delete(int k) {
    if (head == null) {
      System.out.println("List is empty.");
      return;
    }

    Node current = head;
    Node previous = null;

    while (current.next != current) {
      // Move k-1 steps
      for (int i = 1; i < k; i++) {
        previous = current;
        current = current.next;
      }

      // Eliminate the kth person
      previous.next = current.next;
      current = previous.next;
    }

    System.out.println("The survivor is at position: " + current.data);
  }
}

public class JosephusProblem {
  public static void main(String[] args) {
    int n = 41; // Number of people
    int k = 3; // Eliminate every 3rd person

    CircularLinkedList list = new CircularLinkedList();
    for (int i = 1; i <= n; i++) {
      list.insert(i);
    }

    list.delete(k);
  }
}

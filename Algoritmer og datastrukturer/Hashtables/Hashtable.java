import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Hashtable {
  private NameHash[] table;
  private int collition;

  public Hashtable(int tablelength){
    table = new NameHash[tablelength];
    collition = 0;
  }

  public int makeHashKey(String name){
    int sum = 0;
    int counter = 1;
    for (char c : name.toCharArray()) {
      sum += c * counter;
      counter++;
    }
    return sum % table.length;
  }

  public void putInTable(NameHash student){
    int index = makeHashKey(student.getName());
    if (table[index] == null){
      table[index] = student;
    } else {
      NameHash collitionHash = table[index];
      collition++;
      String s = "Collition at index: " + index + ", " + collitionHash.getName() + " -> ";
      while (collitionHash.getNext() != null){
        collitionHash = collitionHash.getNext();
        collition++;
        s += collitionHash.getName() + " -> ";

      }
      System.out.println(s + student);
      collitionHash.setNext(student);
    }
  }

  public NameHash findStudent(String name){
    try {
      int index = makeHashKey(name);
      NameHash student = table[index];
      if (student.getName().trim().equalsIgnoreCase(name)) {
        return student;
      } else {
        while (!student.getName().trim().equalsIgnoreCase(name)) {
          student = student.getNext();
        }
      }
      return student;
    } catch (NullPointerException e){
      System.out.println("Student not found");
      return null;
    }
  }

  public int nrOfStudents(){
    int nrOfStudents = 0;
    NameHash temp;
    for (int i = 0; i < table.length; i++){
      temp = table[i];
      if (temp != null) {
        nrOfStudents++;
        while (temp.getNext() != null){
          nrOfStudents++;
          temp = temp.getNext();
        }
      }
    }
    return nrOfStudents;
  }

  public int getCollition() {
    return collition;
  }

  public int tableLength(){
    return table.length;
  }

  public int nrOfIndexesUsed(){
    int nrOfIndexesUsed = 0;
    for (NameHash name : table){
      if (name != null){
        nrOfIndexesUsed++;
      }
    }
    return nrOfIndexesUsed;
  }

  public static void main(String[] args) {
    Hashtable table = new Hashtable(170);
    try {
      FileReader reader = new FileReader("src/navn.txt");
      BufferedReader br = new BufferedReader(reader);
      String line;
      while ((line = br.readLine()) != null){
        NameHash newStudent = new NameHash(line);
        table.putInTable(newStudent);
      }
      System.out.println(table.findStudent("Helle Vosmik Rosenlind").toString());
      System.out.println("Number of students: " + table.nrOfStudents() + "\n" + "collisions: " +
          table.getCollition());
      System.out.println("Load factor: " + (double)table.nrOfIndexesUsed()/table.tableLength());
      System.out.println("Average: " + (double)table.getCollition()/table.nrOfStudents());

      br.close();
    } catch (IOException e){
      System.out.println(e.getMessage());
    }
  }
}



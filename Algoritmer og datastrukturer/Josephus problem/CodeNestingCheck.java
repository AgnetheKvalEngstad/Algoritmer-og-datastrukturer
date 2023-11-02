import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class CodeNestingCheck {

  public static void checkNesting(String fileName) {
    Stack<Character> stack = new Stack<>();
    String openBrackets = "([{";
    String closeBrackets = ")]}";
    int lineNumber = 1;

    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        for (char character : line.toCharArray()) {
          if (openBrackets.contains(String.valueOf(character))) {
            stack.push(character);
          } else if (closeBrackets.contains(String.valueOf(character))) {
            if (stack.isEmpty()) {
              System.out.println("Error: " + character + " - These don't match");
              return;
            }
            char lastOpen = stack.pop();
            if (!matches(lastOpen, character)) {
              System.out.println("Error: " + character + " - These don't match");
              return;
            }
          }
        }
        lineNumber++;
      }

      if (!stack.isEmpty()) {
        char lastOpen = stack.pop();
        System.out.println("Error: " + lastOpen + " - Missing closing " + oppositeBracket(lastOpen));
      }

      System.out.println("The program is correctly nested.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static boolean matches(char openBracket, char closeBracket) {
    return (openBracket == '(' && closeBracket == ')') ||
        (openBracket == '[' && closeBracket == ']') ||
        (openBracket == '{' && closeBracket == '}');
  }

  private static char oppositeBracket(char bracket) {
    switch (bracket) {
      case ')':
        return '(';
      case ']':
        return '[';
      case '}':
        return '{';
      default:
        return ' ';
    }
  }

  public static void main(String[] args) {
    String fileName = "Source"; // Replace with the path to the source code file you want to check
    checkNesting(fileName);
  }
}

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class CompressDecompress {

    public static void main(String[] args) throws URISyntaxException, IOException {
      Decompress decompress = new Decompress();
      Compress compress = new Compress();

      Scanner sc = new Scanner(System.in);
      System.out.println("What file do you want to process? (Format: name.type)");
      String fileName = sc.nextLine();
      URL resourceUrl1 = CompressDecompress.class.getResource(fileName);
      File fileInput = new File(resourceUrl1.toURI());

      System.out.println("Input 1 for decompression and input 2 for compression");
      int choice = sc.nextInt();
      sc.nextLine();

      switch (choice) {
        case 1 -> {
          System.out.println("In what format would you like the file returned? (eg. .txt)");
          String extension = sc.nextLine();
          File unpackedFile = File.createTempFile(fileInput.getName() + "Unpacked", ".txt", new File(fileInput.getParent()));
          File unpackedFileFinal = File.createTempFile(fileInput.getName() + "UnpackedFinal", extension, new File(fileInput.getParent()));
          Decompress.decompress(fileInput, unpackedFile, unpackedFileFinal);
          unpackedFile.deleteOnExit();
        }
        case 2 -> {
          File fileOut = File.createTempFile(fileInput.getName() + "Compressed", ".txt", new File(fileInput.getParent()));
          File fileOutFinal = File.createTempFile(fileInput.getName() + "CompressedFinal", ".txt", new File(fileInput.getParent()));
          Compress.compress(fileInput, fileOut, fileOutFinal);
          fileOut.deleteOnExit();
        }
        default -> {
          System.out.println("You seem to have entered an incompatible value, goodbye!");
          System.exit(0);
        }
      }


    }
  }

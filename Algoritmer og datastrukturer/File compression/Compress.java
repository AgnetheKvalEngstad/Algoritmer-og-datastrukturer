import java.io.File;
import java.io.IOException;

public class Compress {
  public static void compress(File inputFile, File outputFile, File fileFinal) throws IOException {
    LempelZiv.compress(inputFile, outputFile);
    Huffman.compressData(outputFile, fileFinal);

  }

}

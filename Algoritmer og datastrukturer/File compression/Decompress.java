import java.io.File;
import java.io.IOException;

public class Decompress {
  public static void decompress(File inputFile, File outputFile, File fileFinal) throws IOException {
    Huffman.decompressData(inputFile, outputFile);
      LempelZiv.decompress(outputFile, fileFinal);
  }
}

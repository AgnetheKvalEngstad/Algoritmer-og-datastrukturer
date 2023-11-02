import java.io.*;

public class LempelZiv {

  static byte[] buffer;
  static byte[] data;
  static int length = 0;
  static int position = 0;

  public static void compress(File inputFile, File outputFile) throws IOException {
    DataInputStream input = new DataInputStream(new FileInputStream(inputFile));
    DataOutputStream output = new DataOutputStream(new FileOutputStream(outputFile));

    buffer = new byte[1024*32];
    data = input.readAllBytes();

    for(int i = 0; i < data.length; i++){
      if(length > 32000){
        output.writeShort(length);
        for(int k = 0; k < length; k++){
          output.write(buffer[(position+k)%buffer.length]);
        }
        position = (position+length)%buffer.length;
        output.writeShort(0);
        output.writeShort(0);

        length = 0;

      }
      int bufferPos = inBufferArray(data[i], i);
      if(bufferPos == -1){
        updateBuffer(data[i], i);
        length++;

      }
      else {

        int repeatLength = findMatchingLength(bufferPos, i, i);
        int currentMaxIdx = bufferPos;

        while (true){
          bufferPos = inBufferArray(data[i], bufferPos-1);
          if (bufferPos == -1) break;

          int checkSize = findMatchingLength(bufferPos, i, i);
          if(repeatLength<checkSize){
            repeatLength = checkSize;
            currentMaxIdx = bufferPos;
          }
        }

        if(repeatLength > 6){

          output.writeShort(length);
          for(int k = 0; k < length; k++){
            output.write(buffer[(position+k)%buffer.length]);
          }
          position = (position+length+repeatLength)%buffer.length;
          length = 0;

          output.writeShort(currentMaxIdx);
          output.writeShort(repeatLength);

          for(int j = 0; j<repeatLength; j++){
            updateBuffer(data[i], i);
            i++;
          }
          i--;
        }
        else {
          updateBuffer(data[i], i);
          length++;
        }
      }
    }
    output.writeShort(length);
    for(int k = 0; k < length; k++){
      output.write(buffer[(position+k)%buffer.length]);
    }

    input.close();
    output.close();
  }


  public static void updateBuffer(byte b, int pos){
    buffer[pos%buffer.length] = b;
  }

  public static int inBufferArray(byte b, int pos){
    for(int i = pos%buffer.length; i>=0; i--){
      if (buffer[i] == b)
        return i;
    }
    return -1;
  }

  public static int findMatchingLength(int bufferPos, int bytePos, int posMax){
    byte bufferByte = buffer[bufferPos];
    byte dataByte = data[bytePos];
    int size = 0;

    while (dataByte == bufferByte && bytePos != data.length-1 && bufferPos < posMax){
      size++;
      dataByte = data[++bytePos];
      bufferByte = buffer[++bufferPos%buffer.length];
    }
    
    return size;
  }


  public static void decompress(File inputFile, File fileout) throws IOException {
    DataInputStream in = new DataInputStream(new FileInputStream(inputFile));
    DataOutputStream out = new DataOutputStream(new FileOutputStream(fileout));


    position = 0;
    buffer = new byte[1024*32];
    short start = in.readShort();
    data = new byte[start];
    in.readFully(data);
    out.write(data);

    for(int j = 0; j<start; j++){
      updateBuffer(data[j], j);
      position++;
    }


    while (in.available()>0){
      short back = in.readShort();
      short copyAmount = in.readShort();

      if(copyAmount != 0){
        data = new byte[copyAmount];
      }

      int i = 0;

      for(int tempIndex = back; tempIndex < back + copyAmount; tempIndex++){
        byte index = buffer[tempIndex];
        data[i++] = index;
        updateBuffer(index, position);
        position++;
      }
      if(copyAmount != 0){
        out.write(data);
      }


      start = in.readShort();
      data = new byte[start];
      in.readFully(data);
      for(int j = 0; j<start; j++){
        updateBuffer(data[j], position);
        position++;
      }
      out.write(data);

    }
    in.close();
    out.close();

  }

}
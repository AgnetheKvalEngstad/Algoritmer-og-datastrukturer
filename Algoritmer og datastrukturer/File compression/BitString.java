public class BitString {
  int length;
  long bits;

  public BitString() {
  }

  public BitString(int length, long bits) {
    this.length = length;
    this.bits = bits;
  }

  public BitString(int length, byte bits) {
    this.length = length;
    this.bits = convertByteToLong(bits, length);
  }

  public static BitString combine(BitString bitString1, BitString bitString2) {
    BitString newByte = new BitString();
    newByte.length = bitString1.length + bitString2.length;
    if (newByte.length > 64) {
      System.out.println("BitString too long!");
      return null;
    }
    newByte.bits = bitString2.bits | (bitString1.bits << bitString2.length);
    return newByte;
  }

  public long convertByteToLong(byte bits, int length) {
    long result = 0;
    for (long i = 1L << length - 1; i != 0; i >>= 1) {
      if ((bits & i) == 0) {
        result = (result << 1);
      } else result = ((result << 1) | 1);
    }
    return result;
  }

}


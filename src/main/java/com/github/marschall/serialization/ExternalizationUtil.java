package com.github.marschall.serialization;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.BitSet;

final class ExternalizationUtil {

  static void writeInteger(ObjectOutput out, Integer value) throws IOException {
    if (value == null) {
      out.writeInt(Integer.MAX_VALUE);
    } else {
      out.writeInt(value);
    }
  }

  static void writeLong(ObjectOutput out, Long value) throws IOException {
    if (value == null) {
      out.writeLong(Long.MAX_VALUE);
    } else {
      out.writeLong(value);
    }
  }

  static void writeString(ObjectOutput out, String value) throws IOException {
    // NULL and "" are the same in Oracle
    if (value == null) {
      out.writeUTF("");
    } else {
      out.writeUTF(value);
    }
  }
  
  static void writeBigDecimal(ObjectOutput out, BigDecimal value) throws IOException {
    if (value == null) {
      out.writeByte(-1);
    } else {
      int scale = value.scale();
      if (scale > Byte.MAX_VALUE) {
        throw new IllegalArgumentException("unsupported big integer scale");
      }
      out.writeByte(scale);
      byte[] byteArray = value.unscaledValue().toByteArray();
      int arrayLength = byteArray.length;
      if (arrayLength > Byte.MAX_VALUE) {
        throw new IllegalArgumentException("unsupported big integer size");
      }
      out.writeByte(arrayLength);
      out.write(byteArray);
    }
  }
  
  static void writeBitSet(ObjectOutput out, BitSet value, int bitSetSize) throws IOException {
    int byteIndex = 0;
    int flagsByteSize = flagsByteSize(bitSetSize);
    for (int globalBitIndex = 0; globalBitIndex < Constants.BIT_SET_SIZE; globalBitIndex += 8) {
      int end;
      if (byteIndex * 8 > flagsByteSize) {
        end = byteIndex * 8 - flagsByteSize;
      } else {
        end = 7;
      }
      int b = 0;
      for (int localBitIndex = 0; localBitIndex < end; ++localBitIndex) {
        if (value.get(globalBitIndex + localBitIndex)) {
          b |= 1 << localBitIndex;
        }
      }
      out.write(b);
      byteIndex += 1;
    }
  }
  

  // TODO generate as well
  static int flagsByteSize(int bitSetSize) {
    int candidate = bitSetSize / 8;
    if (candidate * 8 == bitSetSize) {
      return candidate;
    } else {
      return candidate + 1;
    }
  }
  
  static Integer readInteger(ObjectInput in) throws IOException {
    int value = in.readInt();
    if (value == Integer.MAX_VALUE) {
      return null;
    } else {
      return value;
    }
  }
  
  static Long readLong(ObjectInput in) throws IOException {
    long value = in.readLong();
    if (value == Long.MAX_VALUE) {
      return null;
    } else {
      return value;
    }
  }
  
  static String readString(ObjectInput in) throws IOException {
    // NULL and "" are the same in Oracle
    String value = in.readUTF();
    if (value.length() == 0) {
      return null;
    } else {
      return value;
    }
  }
  
  static BigDecimal readBigDecimal(ObjectInput in) throws IOException {
    int scale = in.readByte();
    if (scale == -1) {
      return null;
    } else {
      int length = in.readByte();
      byte[] value = new byte[length];
      in.readFully(value);
      BigInteger uncsaled = new BigInteger(value);
      return new BigDecimal(uncsaled, scale);
    }
  }
  
  static void initializeBitSet(ObjectInput in, BitSet flags, int bitSetSize) throws IOException {
    int byteIndex = 0;
    int flagsByteSize = flagsByteSize(bitSetSize);
    for (int globalBitIndex = 0; globalBitIndex < Constants.BIT_SET_SIZE; globalBitIndex += 8) {
      int end;
      if (byteIndex * 8 > flagsByteSize) {
        end = byteIndex * 8 - flagsByteSize;
      } else {
        end = 7;
      }
      int b = in.readUnsignedByte();
      for (int localBitIndex = 0; localBitIndex < end; ++localBitIndex) {
        boolean isSet = (b & 1 << localBitIndex) != 0;
        flags.set(localBitIndex + globalBitIndex, isSet);
      }
      byteIndex += 1;
    }
  }

}

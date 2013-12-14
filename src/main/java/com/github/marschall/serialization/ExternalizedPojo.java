package com.github.marschall.serialization;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class ExternalizedPojo implements Externalizable, WritablePojo {

  private static final long serialVersionUID = 1L;

  private Integer value1;
  private Long value2;
  private String value3;
  private BigDecimal value4;
  private BitSet flags = new BitSet(Constants.BIT_SET_SIZE);

  public Integer getValue1() {
    return value1;
  }

  @Override
  public void setValue1(Integer value1) {
    this.value1 = value1;
  }

  public Long getValue2() {
    return value2;
  }

  @Override
  public void setValue2(Long value2) {
    this.value2 = value2;
  }

  public String getValue3() {
    return value3;
  }

  @Override
  public void setValue3(String value3) {
    this.value3 = value3;
  }

  public BigDecimal getValue4() {
    return value4;
  }

  @Override
  public void setValue4(BigDecimal value4) {
    this.value4 = value4;
  }


  @Override
  public BitSet getFlags() {
    return flags;
  }


  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeLong(serialVersionUID);

    // Integer
    if (value1 == null) {
      out.writeInt(Integer.MAX_VALUE);
    } else {
      out.writeInt(value1);
    }

    // Long
    if (value2 == null) {
      out.writeLong(Long.MAX_VALUE);
    } else {
      out.writeLong(value2);
    }

    // String
    // NULL and "" are the same in Oracle
    if (value3 == null) {
      out.writeUTF("");
    } else {
      out.writeUTF(value3);
    }

    // BigDecimal
    if (value4 == null) {
      out.writeByte(-1);
    } else {
      int scale = value4.scale();
      if (scale > Byte.MAX_VALUE) {
        throw new IllegalArgumentException("unsupported big integer scale");
      }
      out.writeByte(scale);
      byte[] byteArray = value4.unscaledValue().toByteArray();
      int arrayLength = byteArray.length;
      if (arrayLength > Byte.MAX_VALUE) {
        throw new IllegalArgumentException("unsupported big integer size");
      }
      out.writeByte(arrayLength);
      out.write(byteArray);
    }
    
    // BitSet
    int byteIndex = 0;
    int flagsArraySize = getFlagsArraySize();
    for (int globalBitIndex = 0; globalBitIndex < flagsArraySize; globalBitIndex += 8) {
      int end;
      if (byteIndex * 8 > flagsArraySize) {
        end = byteIndex * 8 - flagsArraySize;
      } else {
        end = 8;
      }
      int b = 0;
      for (int localBitIndex = 0; localBitIndex < end; ++localBitIndex) {
        if (this.flags.get(globalBitIndex + localBitIndex)) {
          b |= 1 << localBitIndex;
        }
      }
      out.write(b);
      byteIndex += 1;
    }
  }


  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    if (in.readLong() != serialVersionUID) {
      throw new InvalidClassException("incompatible class version");
    }

    // Integer
    int intVal = in.readInt();
    if (intVal == Integer.MAX_VALUE) {
      this.value1 = null;
    } else {
      this.value1 = intVal;
    }

    // Long
    long longVal = in.readLong();
    if (longVal == Long.MAX_VALUE) {
      this.value2 = null;
    } else {
      this.value2 = longVal;
    }

    // String
    // NULL and "" are the same in Oracle
    String stringVal = in.readUTF();
    if (stringVal.length() == 0) {
      this.value3 = null;
    } else {
      this.value3 = stringVal;
    }

    int scale = in.readByte();
    if (scale == -1) {
      this.value4 = null;
    } else {
      int length = in.readByte();
      byte[] value = new byte[length];
      in.readFully(value);
      BigInteger uncsaled = new BigInteger(value);
      this.value4 = new BigDecimal(uncsaled, scale);
    }

    // BitSet
    int byteIndex = 0;
    int flagsArraySize = getFlagsArraySize();
    for (int globalBitIndex = 0; globalBitIndex < flagsArraySize; globalBitIndex += 8) {
      int end;
      if (byteIndex * 8 > flagsArraySize) {
        end = byteIndex * 8 - flagsArraySize;
      } else {
        end = 8;
      }
      int b = in.readUnsignedByte();
      for (int localBitIndex = 0; localBitIndex < end; ++localBitIndex) {
        boolean isSet = (b |= 1 << localBitIndex) == 1;
        this.flags.set(localBitIndex + globalBitIndex, isSet);
      }
      byteIndex += 1;
    }
  }
  
  static int getFlagsArraySize() {
    int candidate = Constants.BIT_SET_SIZE / 8;
    if (candidate * 8 == Constants.BIT_SET_SIZE) {
      return candidate;
    } else {
      return candidate + 1;
    }
  }

  static long computeSerialVersionUID() {
    // http://docs.oracle.com/javase/6/docs/platform/serialization/spec/class.html#4100
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-1");

      digest.update("NUMBER(5)".getBytes(UTF_8));
      digest.update("value1".getBytes(UTF_8));

      digest.update("NUMBER(10)".getBytes(UTF_8));
      digest.update("value2".getBytes(UTF_8));

      digest.update("VARCHAR2(20)".getBytes(UTF_8));
      digest.update("value3".getBytes(UTF_8));

      digest.update("NUMBER(20)".getBytes(UTF_8));
      digest.update("value4".getBytes(UTF_8));

      byte[] sha = digest.digest();


      long hash = (sha[0] & 0xFF) |
          (sha[1] & 0xFF) << 8 |
          (sha[2] & 0xFF) << 16 |
          (sha[3] & 0xFF) << 24 |
          (sha[4] & 0xFF) << 32 |
          (sha[5] & 0xFF) << 40 |
          (sha[6] & 0xFF) << 48 |
          (sha[7] & 0xFF) << 56;
      return hash;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("sha-1 not supported", e);
    }
  }

}

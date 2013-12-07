package com.github.marschall.serialization;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class ExternalizedPojo implements Externalizable {

  private static final long serialVersionUID = 1L;

  private Integer value1;
  private Long value2;
  private String value3;
  private BigDecimal value4;
  private BitSet flags;

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

    // TODO Auto-generated method stub

  }


  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    if (in.readLong() != serialVersionUID) {
      throw new InvalidClassException("incompatible class version");
    }

    int intVal = in.readInt();
    if (intVal == Integer.MAX_VALUE) {
      this.value1 = null;
    } else {
      this.value1 = intVal;
    }

    long longVal = in.readLong();
    if (longVal == Long.MAX_VALUE) {
      this.value2 = null;
    } else {
      this.value2 = longVal;
    }
    
    String stringVal = in.readUTF();
    if (stringVal.length() == 0) {
      this.value3 = null;
    } else {
      this.value3 = stringVal;
    }

    // TODO Auto-generated method stub

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

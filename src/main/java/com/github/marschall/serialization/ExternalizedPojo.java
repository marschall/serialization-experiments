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

public class ExternalizedPojo implements Externalizable, WritablePojo {

  private static final long serialVersionUID = 1L;

  private Integer value1;
  private Long value2;
  private String value3;
  private BigDecimal value4;
  private final BitSet flags = new BitSet(Constants.BIT_SET_SIZE);

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

    ExternalizationUtil.writeInteger(out, this.value1);
    ExternalizationUtil.writeLong(out, this.value2);
    ExternalizationUtil.writeString(out, this.value3);
    ExternalizationUtil.writeBigDecimal(out, this.value4);
    ExternalizationUtil.writeBitSet(out, this.flags, Constants.BIT_SET_SIZE);
  }


  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    if (in.readLong() != serialVersionUID) {
      throw new InvalidClassException("incompatible class version");
    }

    this.value1 = ExternalizationUtil.readInteger(in);
    this.value2 = ExternalizationUtil.readLong(in);
    this.value3 = ExternalizationUtil.readString(in);
    this.value4 = ExternalizationUtil.readBigDecimal(in);
    ExternalizationUtil.initializeBitSet(in, flags, Constants.BIT_SET_SIZE);
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

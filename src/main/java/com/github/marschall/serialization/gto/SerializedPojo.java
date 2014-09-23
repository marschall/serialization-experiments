package com.github.marschall.serialization.gto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Date;

public class SerializedPojo implements Serializable, WritablePojo {

  private Integer value1;
  private Long value2;
  private String value3;
  private BigDecimal value4;
  private Date value5;
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

  public Date getValue5() {
    return value5;
  }

  public void setValue5(Date value5) {
    this.value5 = value5;
  }

  @Override
  public BitSet getFlags() {
    return flags;
  }


}

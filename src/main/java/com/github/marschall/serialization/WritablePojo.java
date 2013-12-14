package com.github.marschall.serialization;

import java.math.BigDecimal;
import java.util.BitSet;

public interface WritablePojo {

  void setValue1(Integer value1);

  void setValue2(Long value2);

  void setValue3(String value3);

  void setValue4(BigDecimal value4);

  BitSet getFlags();

}
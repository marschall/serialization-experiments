package com.github.marschall.serialization.gto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

final class PojoUtils {

  static void initialize(WritablePojo pojo) {
    // avoid Integer.valueOf cache
    pojo.setValue1(1000);
    // avoid Long.valueOf cache
    pojo.setValue2(100000000000L);
    // avoid string intern pool cache
    pojo.setValue3(new String("abcdefghijklmnopqrstuvwxyz"));
    // create a new instance to void object output stream cache
    pojo.setValue4(new BigDecimal("12345678901234567890.12"));
    BitSet flags = pojo.getFlags();
    for (int i = 0; i < Constants.BIT_SET_SIZE; ++i) {
      if (i % 2 == 0) {
        flags.set(i);
      }
    }
  }
  
  static <T extends WritablePojo> List<T> generatePojoList(Class<T> clazz, int size) {
    List<T> pojos = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      T pojo = null;
      if (clazz == ExternalizedPojo.class) {
        pojo = clazz.cast(new ExternalizedPojo());
      } else if (clazz == SerializedPojo.class) {
        pojo = clazz.cast(new SerializedPojo());
      } else {
        throw new IllegalArgumentException("unknown pojo class: " + clazz);
      }
      initialize(pojo);
      pojos.add(pojo);
    }
    return pojos;
    
  }

}

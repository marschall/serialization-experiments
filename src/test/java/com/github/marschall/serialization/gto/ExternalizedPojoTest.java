package com.github.marschall.serialization.gto;

import static com.github.marschall.serialization.SerializationUtil.dersialize;
import static com.github.marschall.serialization.SerializationUtil.serialize;
import static com.github.marschall.serialization.gto.IsBitSetMatcher.isBitSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class ExternalizedPojoTest {

  @Test
  public void nullFields() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.setValue1(null);
    pojo.setValue2(null);
    pojo.setValue3(null);
    pojo.setValue4(null);
    pojo.setValue5(null);
    ExternalizedPojo readBack = this.copy(pojo);
    assertNotNull(readBack);
    assertNull(readBack.getValue1());
    assertNull(readBack.getValue2());
    assertNull(readBack.getValue3());
    assertNull(readBack.getValue4());
    assertNull(readBack.getValue5());
    assertNotNull(readBack.getFlags());
  }

  @Test
  public void notNullFields() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.setValue1(1);
    pojo.setValue2(100000000000L);
    pojo.setValue3("abcd");
    pojo.setValue4(new BigDecimal("10000000000000.00"));
    long now = System.currentTimeMillis();
    pojo.setValue5(new Date(now));
    ExternalizedPojo readBack = this.copy(pojo);
    assertNotNull(readBack);
    assertEquals(Integer.valueOf(1), readBack.getValue1());
    assertEquals(Long.valueOf(100000000000L), readBack.getValue2());
    assertEquals("abcd", readBack.getValue3());
    assertEquals(new BigDecimal("10000000000000.00"), readBack.getValue4());
    assertEquals(new Date(now), readBack.getValue5());
    assertNotNull(readBack.getFlags());
  }

  @Test
  public void negativeValue() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.setValue1(-1);
    pojo.setValue2(-100000000000L);
    pojo.setValue4(new BigDecimal("-10000000000000.00"));
    long now = -1L;
    pojo.setValue5(new Date(now));
    ExternalizedPojo readBack = this.copy(pojo);
    assertNotNull(readBack);
    assertEquals(Integer.valueOf(-1), readBack.getValue1());
    assertEquals(Long.valueOf(-100000000000L), readBack.getValue2());
    assertEquals(new BigDecimal("-10000000000000.00"), readBack.getValue4());
    assertEquals(new Date(now), readBack.getValue5());
    assertNotNull(readBack.getFlags());
  }

  @Test
  public void unicodeString() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    String s = "\uD83C\uDF54";
    pojo.setValue3(s);
    ExternalizedPojo readBack = this.copy(pojo);
    assertNotNull(readBack);
    assertEquals(s, readBack.getValue3());
  }

  @Test
  public void bitSetNoneSet() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.getFlags().clear();
    ExternalizedPojo readBack = this.copy(pojo);
    assertNotNull(readBack);
    BitSet flags = readBack.getFlags();
    assertNotNull(flags);
    assertTrue(flags.isEmpty());
    for (int i = 0; i < Constants.BIT_SET_SIZE; ++i) {
      assertThat(flags, not(isBitSet(i)));
    }
    assertThat(flags, not(isBitSet(Constants.BIT_SET_SIZE)));
    //    assertEquals(Constants.BIT_SET_SIZE, flags.size());
  }

  @Test
  public void bitSetAllSet() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.getFlags().clear();
    ExternalizedPojo readBack = this.copy(pojo);
    assertNotNull(readBack);
    BitSet flags = readBack.getFlags();
    flags.set(0, Constants.BIT_SET_SIZE, true);
    assertNotNull(flags);
    assertFalse(flags.isEmpty());
    for (int i = 0; i < Constants.BIT_SET_SIZE; ++i) {
      assertThat(flags, isBitSet(i));
    }
    assertThat(flags, not(isBitSet(Constants.BIT_SET_SIZE)));
    //    assertEquals(Constants.BIT_SET_SIZE, flags.size());
  }

  @Test
  public void onlyFirstByte() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.getFlags().clear();
    ExternalizedPojo readBack = this.copy(pojo);
    assertNotNull(readBack);
    BitSet flags = readBack.getFlags();
    flags.set(0, true);
    flags.set(7, true);
    assertNotNull(flags);
    assertFalse(flags.isEmpty());
    for (int i = 0; i < Constants.BIT_SET_SIZE; ++i) {
      if ((i != 0) && (i != 7)) {
        assertThat(flags, not(isBitSet(i)));
      } else {
        assertThat(flags, isBitSet(i));
      }
    }
    assertThat(flags, not(isBitSet(Constants.BIT_SET_SIZE)));
  }

  @Test
  public void onlyLastByte() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.getFlags().clear();
    ExternalizedPojo readBack = this.copy(pojo);
    assertNotNull(readBack);
    BitSet flags = readBack.getFlags();
    flags.set(64, true);
    flags.set(Constants.BIT_SET_SIZE - 1, true);
    assertNotNull(flags);
    assertFalse(flags.isEmpty());
    for (int i = 0; i < Constants.BIT_SET_SIZE; ++i) {
      if ((i != 64) && (i != (Constants.BIT_SET_SIZE - 1))) {
        assertThat(flags, not(isBitSet(i)));
      } else {
        assertThat(flags, isBitSet(i));
      }
    }
    assertThat(flags, not(isBitSet(Constants.BIT_SET_SIZE)));
  }

  private ExternalizedPojo copy(ExternalizedPojo pojo) throws IOException, ClassNotFoundException {
    byte[] data = serialize(pojo);
    return (ExternalizedPojo) dersialize(data);
  }

}

package com.github.marschall.serialization;

import static com.github.marschall.serialization.IsBitSetMatcher.isBitSet;
import static com.github.marschall.serialization.SerializationUtil.dersialize;
import static com.github.marschall.serialization.SerializationUtil.serialize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.BitSet;

import org.junit.Test;

public class ExternalizedPojoTest {

  @Test
  public void nullFields() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.setValue1(null);
    pojo.setValue2(null);
    pojo.setValue3(null);
    pojo.setValue4(null);
    ExternalizedPojo readBack = copy(pojo);
    assertNotNull(readBack);
    assertNull(readBack.getValue1());
    assertNull(readBack.getValue2());
    assertNull(readBack.getValue3());
    assertNull(readBack.getValue4());
    assertNotNull(readBack.getFlags());
  }
  
  @Test
  public void notNullFields() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.setValue1(1);
    pojo.setValue2(100000000000L);
    pojo.setValue3("abcd");
    pojo.setValue4(new BigDecimal("10000000000000.00"));
    ExternalizedPojo readBack = copy(pojo);
    assertNotNull(readBack);
    assertEquals(Integer.valueOf(1), readBack.getValue1());
    assertEquals(Long.valueOf(100000000000L), readBack.getValue2());
    assertEquals("abcd", readBack.getValue3());
    assertEquals(new BigDecimal("10000000000000.00"), readBack.getValue4());
    assertNotNull(readBack.getFlags());
  }
  
  @Test
  public void negativeValue() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.setValue1(-1);
    pojo.setValue2(-100000000000L);
    pojo.setValue4(new BigDecimal("-10000000000000.00"));
    ExternalizedPojo readBack = copy(pojo);
    assertNotNull(readBack);
    assertEquals(Integer.valueOf(-1), readBack.getValue1());
    assertEquals(Long.valueOf(-100000000000L), readBack.getValue2());
    assertEquals(new BigDecimal("-10000000000000.00"), readBack.getValue4());
    assertNotNull(readBack.getFlags());
  }
  
  @Test
  public void bitSetNoneSet() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    pojo.getFlags().clear();
    ExternalizedPojo readBack = copy(pojo);
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
    ExternalizedPojo readBack = copy(pojo);
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
  
  private ExternalizedPojo copy(ExternalizedPojo pojo) throws IOException, ClassNotFoundException {
    byte[] data = serialize(pojo);
    return (ExternalizedPojo) dersialize(data);
  }

}

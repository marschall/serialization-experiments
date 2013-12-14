package com.github.marschall.serialization;

import static com.github.marschall.serialization.SerializationUtil.dersialize;
import static com.github.marschall.serialization.SerializationUtil.serialize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

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
  
  private ExternalizedPojo copy(ExternalizedPojo pojo) throws IOException, ClassNotFoundException {
    byte[] data = serialize(pojo);
    return (ExternalizedPojo) dersialize(data);
  }

}

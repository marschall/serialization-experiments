package com.github.marschall.serialization;

import static com.github.marschall.serialization.SerializationUtil.dersialize;
import static com.github.marschall.serialization.SerializationUtil.serialize;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

public class ExternalizedPojoTest {

  @Test
  public void simpleSerialization() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    byte[] data = serialize(pojo);
    WritablePojo readBack = (WritablePojo) dersialize(data);
    assertNotNull(readBack);
  }

}

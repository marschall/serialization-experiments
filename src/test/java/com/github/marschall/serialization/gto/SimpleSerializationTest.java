package com.github.marschall.serialization.gto;

import static com.github.marschall.serialization.SerializationUtil.dersialize;
import static com.github.marschall.serialization.SerializationUtil.serialize;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SimpleSerializationTest {

  private final WritablePojo pojo;

  public SimpleSerializationTest(WritablePojo pojo) {
    this.pojo = pojo;
  }

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[]{new SerializedPojo()},
        new Object[]{new ExternalizedPojo()});
  }

  @Test
  public void simpleSerialization() throws IOException, ClassNotFoundException {
    byte[] data = serialize(pojo);
    WritablePojo readBack = (WritablePojo) dersialize(data);
    assertNotNull(readBack);
  }

}

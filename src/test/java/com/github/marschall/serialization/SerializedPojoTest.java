package com.github.marschall.serialization;

import static com.github.marschall.serialization.SerializationUtil.dersialize;
import static com.github.marschall.serialization.SerializationUtil.serialize;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Test;

public class SerializedPojoTest {

  @Test
  public void simpleSerialization() throws IOException, ClassNotFoundException {
    SerializedPojo pojo = new SerializedPojo();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
      objectStream.writeObject(pojo);
    }
    byte[] data = serialize(pojo);
    SerializedPojo readBack = (SerializedPojo) dersialize(data);
    assertNotNull(readBack);
  }

}

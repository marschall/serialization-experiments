package com.github.marschall.serialization;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    byte[] data = outputStream.toByteArray();
    SerializedPojo readBack;
    try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
      readBack = (SerializedPojo) objectStream.readObject();
    }
    assertNotNull(readBack);
  }

}

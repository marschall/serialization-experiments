package com.github.marschall.serialization;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

public class ExternalizedPojoTest {

  @Test
  public void simpleSerialization() throws IOException, ClassNotFoundException {
    ExternalizedPojo pojo = new ExternalizedPojo();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
      objectStream.writeObject(pojo);
    }
    byte[] data = outputStream.toByteArray();
    ExternalizedPojo readBack;
    try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
      readBack = (ExternalizedPojo) objectStream.readObject();
    }
    assertNotNull(readBack);
  }

}

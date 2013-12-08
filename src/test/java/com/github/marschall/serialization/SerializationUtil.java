package com.github.marschall.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

final class SerializationUtil {

  static byte[] serialize(Serializable object) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
      objectStream.writeObject(object);
    }
    return outputStream.toByteArray();
  }

  static int serializedSize(Serializable object) throws IOException {
    // TODO optimize
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
      objectStream.writeObject(object);
    }
    return outputStream.size();
  }

  static Object dersialize(byte[] data) throws ClassNotFoundException, IOException {
    try (ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream objectStream = new ObjectInputStream(in)) {
      return objectStream.readObject();
    }
  }

}

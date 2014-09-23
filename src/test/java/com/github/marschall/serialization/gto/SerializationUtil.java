package com.github.marschall.serialization.gto;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import org.codehaus.jackson.map.ObjectMapper;

import com.github.marschall.serialization.gto.CountingOutputStream;
import com.google.gson.Gson;

final class SerializationUtil {

  static byte[] serialize(Serializable object) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
      objectStream.writeObject(object);
    }
    return outputStream.toByteArray();
  }

  static int serializedSize(Serializable object) throws IOException {
    CountingOutputStream outputStream = new CountingOutputStream();
    try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
      objectStream.writeObject(object);
    }
    return outputStream.getCount();
  }

  static Object dersialize(byte[] data) throws ClassNotFoundException, IOException {
    try (ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream objectStream = new ObjectInputStream(in)) {
      return objectStream.readObject();
    }
  }

  static int serializedJacksonSize(Object pojo) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    CountingOutputStream stream = new CountingOutputStream();
    try (OutputStreamWriter writer = new OutputStreamWriter(stream, UTF_8)) {
      objectMapper.writeValue(writer, pojo);
    }
    return stream.getCount();
  }
  
  static int serializedGsonSize(Object pojo) throws IOException {
    Gson gson = new Gson();
    CountingOutputStream stream = new CountingOutputStream();
    try (OutputStreamWriter writer = new OutputStreamWriter(stream, UTF_8)) {
      gson.toJson(pojo, writer);
    }
    return stream.getCount();
  }

}

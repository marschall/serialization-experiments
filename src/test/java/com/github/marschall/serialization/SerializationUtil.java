package com.github.marschall.serialization;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import org.codehaus.jackson.map.ObjectMapper;

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
    OutputStreamWriter writer = new OutputStreamWriter(stream, UTF_8);
    objectMapper.writeValue(writer, pojo);
    return stream.getCount();
  }
  
  static final class CountingOutputStream extends OutputStream {
    
    private int count;

    CountingOutputStream() {
      this.count = 0;
    }
    
    int getCount() {
      return this.count;
    }

    @Override
    public void write(int b) throws IOException {
      this.count += 1;
    }

    @Override
    public void write(byte[] b) throws IOException {
      this.count += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
      if (b == null) {
        throw new NullPointerException();
      } else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
        throw new IndexOutOfBoundsException();
      }
      this.count += len;
    }

  }

}

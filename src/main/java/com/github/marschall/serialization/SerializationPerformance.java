package com.github.marschall.serialization;

import static com.github.marschall.serialization.PojoUtils.generatePojoList;
import static com.github.marschall.serialization.PojoUtils.initialize;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.google.gson.Gson;

@State(Scope.Thread)
public class SerializationPerformance {

  private ObjectMapper objectMapper;
  private Gson gson;
  private List<ExternalizedPojo> externalizedPojos;
  private List<SerializedPojo> serializedPojos;
  private ExternalizedPojo externalizedPojo;
  private SerializedPojo serializedPojo;
  private CountingOutputStream countingOutputStream;

  @Setup
  public void init() throws IOException {
    this.objectMapper = new ObjectMapper();
    this.gson = new Gson();

    externalizedPojo = new ExternalizedPojo();
    initialize(externalizedPojo);
    serializedPojo = new SerializedPojo();
    initialize(serializedPojo);

    int arraySize = 100;
    externalizedPojos = generatePojoList(ExternalizedPojo.class, arraySize);
    serializedPojos = generatePojoList(SerializedPojo.class, arraySize);

    countingOutputStream = new CountingOutputStream();
  }

//  @TearDown
//  public void destroy() throws IOException {
//    writer.close();
//    countingOutputStream.close();
//  }

  @GenerateMicroBenchmark
  public void serializationSingle() throws IOException {
    try (ObjectOutputStream objectStream = new ObjectOutputStream(countingOutputStream)) {
      objectStream.writeObject(this.serializedPojo);
    }
  }

  @GenerateMicroBenchmark
  public void serializationList() throws IOException {
    try (ObjectOutputStream objectStream = new ObjectOutputStream(countingOutputStream)) {
      objectStream.writeObject(this.serializedPojos);
    }
  }

  @GenerateMicroBenchmark
  public void externalizableSingle() throws IOException {
    try (ObjectOutputStream objectStream = new ObjectOutputStream(countingOutputStream)) {
      objectStream.writeObject(this.externalizedPojo);
    }
  }

  @GenerateMicroBenchmark
  public void externalizableList() throws IOException {
    try (ObjectOutputStream objectStream = new ObjectOutputStream(countingOutputStream)) {
      objectStream.writeObject(this.externalizedPojos);
    }
  }

  @GenerateMicroBenchmark
  public void gsonSingle() throws IOException {
    try (OutputStreamWriter writer = new OutputStreamWriter(countingOutputStream, UTF_8)) {
      this.gson.toJson(this.serializedPojo, writer);
    }
  }

  @GenerateMicroBenchmark
  public void gsonList() throws IOException {
    try (OutputStreamWriter writer = new OutputStreamWriter(countingOutputStream, UTF_8)) {
      this.gson.toJson(this.serializedPojos, writer);
    }
  }
  
  @GenerateMicroBenchmark
  public void jacksonSingle() throws IOException {
    try (OutputStreamWriter writer = new OutputStreamWriter(countingOutputStream, UTF_8)) {
      this.objectMapper.writeValue(writer, this.serializedPojo);
    }
  }
  
  @GenerateMicroBenchmark
  public void jacksonList() throws IOException {
    try (OutputStreamWriter writer = new OutputStreamWriter(countingOutputStream, UTF_8)) {
      this.objectMapper.writeValue(writer, this.serializedPojos);
    }
  }

}

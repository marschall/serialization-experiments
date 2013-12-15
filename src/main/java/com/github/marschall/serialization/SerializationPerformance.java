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
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

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
  private OutputStreamWriter writer;
  private ObjectOutputStream objectStream;

  @Setup(Level.Iteration)
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
    writer = new OutputStreamWriter(countingOutputStream, UTF_8);
    objectStream = new ObjectOutputStream(countingOutputStream);
  }

  @TearDown(Level.Iteration)
  public void destroy() throws IOException {
    objectStream.close();
    writer.close();
    countingOutputStream.close();
  }

  @GenerateMicroBenchmark
  public void serializationSingle() throws IOException {
    objectStream.writeObject(this.serializedPojo);
    objectStream.flush();
  }

  @GenerateMicroBenchmark
  public void serializationList() throws IOException {
    objectStream.writeObject(this.serializedPojos);
    objectStream.flush();
  }

  @GenerateMicroBenchmark
  public void externalizableSingle() throws IOException {
    objectStream.writeObject(this.externalizedPojo);
    objectStream.flush();
  }

  @GenerateMicroBenchmark
  public void externalizableList() throws IOException {
    objectStream.writeObject(this.externalizedPojos);
    objectStream.flush();
  }

  @GenerateMicroBenchmark
  public void gsonSingle() throws IOException {
    this.gson.toJson(this.serializedPojo, this.writer);
    this.writer.flush();
  }

  @GenerateMicroBenchmark
  public void gsonList() throws IOException {
    this.gson.toJson(this.serializedPojos, this.writer);
    this.writer.flush();
  }
  
  @GenerateMicroBenchmark
  public void jacksonSingle() throws IOException {
    this.objectMapper.writeValue(this.writer, this.serializedPojo);
    this.writer.flush();
  }
  
  @GenerateMicroBenchmark
  public void jacksonList() throws IOException {
    this.objectMapper.writeValue(this.writer, this.serializedPojos);
    this.writer.flush();
  }

}

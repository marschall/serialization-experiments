package com.github.marschall.serialization.context;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.github.marschall.serialization.CountingOutputStream;

import static com.github.marschall.serialization.context.ContextTOBuilder.newDefaultContextTO;
import static com.github.marschall.serialization.context.ContextTOBuilder.newOptimizedContextTO;
import static org.openjdk.jmh.annotations.Scope.Thread;

@State(Thread)
public class SerializationPerformance {

  private DefaultContextTO defaultContextTO;
  private OptimizedContextTO optimizedContextTO;
  private CountingOutputStream countingOutputStream;

  @Setup
  public void init() {
    optimizedContextTO = newOptimizedContextTO();
    defaultContextTO = newDefaultContextTO();

    countingOutputStream = new CountingOutputStream();
  }

//  @TearDown
//  public void destroy() throws IOException {
//    writer.close();
//    countingOutputStream.close();
//  }

  @Benchmark
  public void optimizedContextTO() throws IOException {
    try (ObjectOutputStream objectStream = new ObjectOutputStream(countingOutputStream)) {
      objectStream.writeObject(this.optimizedContextTO);
    }
  }

  @Benchmark
  public void defaultContextTO() throws IOException {
    try (ObjectOutputStream objectStream = new ObjectOutputStream(countingOutputStream)) {
      objectStream.writeObject(this.defaultContextTO);
    }
  }

}

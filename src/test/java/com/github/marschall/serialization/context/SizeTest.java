package com.github.marschall.serialization.context;

import static com.github.marschall.serialization.SerializationUtil.serializedSize;
import static com.github.marschall.serialization.context.ContextTOBuilder.newDefaultContextTO;
import static com.github.marschall.serialization.context.ContextTOBuilder.newOptimizedContextTO;

import java.io.IOException;

import org.junit.Test;

public class SizeTest {

  @Test
  public void singleObject() throws IOException {
    DefaultContextTO defaultContextTO = newDefaultContextTO();
    OptimizedContextTO optimizedContextTO = newOptimizedContextTO();

    int defaultSize = serializedSize(defaultContextTO);
    int optimizedSize = serializedSize(optimizedContextTO);

    System.out.printf("single%n");
    System.out.printf("------%n");
    System.out.printf("default:\t%, 7d%n", defaultSize);
    System.out.printf("optimized:\t%, 7d%n", optimizedSize);
    System.out.printf("relative:\t%.2f %%%n", ((double) optimizedSize / (double) defaultSize) * 100.0d);
    System.out.printf("=======================%n");
  }

}

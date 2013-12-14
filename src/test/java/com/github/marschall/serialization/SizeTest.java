package com.github.marschall.serialization;

import static com.github.marschall.serialization.SerializationUtil.serializedSize;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static com.github.marschall.serialization.SerializationUtil.serializedJacksonSize;

import org.junit.Test;

public class SizeTest {

  @Test
  public void singleObject() throws IOException {
    ExternalizedPojo externalizedPojo = new ExternalizedPojo();
    initialize(externalizedPojo);
    SerializedPojo serializedPojo = new SerializedPojo();
    initialize(serializedPojo);

    int externalizedSize = serializedSize(externalizedPojo);
    int serializedSize = serializedSize(serializedPojo);
    int jsonSize = serializedJacksonSize(serializedPojo);
    
    System.out.printf("single%n");
    System.out.printf("-------------------------------------------%n");
    System.out.printf("externalized:\t%, 7d%n", externalizedSize);
    System.out.printf("serialized:\t%, 7d%n", serializedSize);
    System.out.printf("JSON:\t\t%, 7d%n", jsonSize);
    System.out.printf("relative:\t%.2f %%%n", ((double) externalizedSize / (double) serializedSize) * 100.0d);
    System.out.printf("============================================%n");
  }

  @Test
  public void multipleObjects() throws IOException {
    List<ExternalizedPojo> externalizedPojos = new ArrayList<>(100);
    List<SerializedPojo> serializedPojos = new ArrayList<>(100);
    for (int i = 0; i < 100; i++) {
      ExternalizedPojo externalizedPojo = new ExternalizedPojo();
      initialize(externalizedPojo);
      externalizedPojos.add(externalizedPojo);

      SerializedPojo serializedPojo = new SerializedPojo();
      initialize(serializedPojo);
      serializedPojos.add(serializedPojo);
    }

    int externalizedSize = serializedSize((Serializable) externalizedPojos);
    int serializedSize = serializedSize((Serializable) serializedPojos);
    int jsonSize = serializedJacksonSize(serializedPojos);

    System.out.printf("list%n");
    System.out.printf("-------------------------------------------%n");
    System.out.printf("externalized:\t%, 7d%n", externalizedSize);
    System.out.printf("serialized:\t%, 7d%n", serializedSize);
    System.out.printf("JSON:\t\t%, 7d%n", jsonSize);
    System.out.printf("relative:\t%.2f %%%n", ((double) externalizedSize / (double) serializedSize) * 100.0d);
    System.out.printf("============================================%n");

  }

  private void initialize(WritablePojo pojo) {
    // avoid Integer.valueOf cache
    pojo.setValue1(1000);
    // avoid Long.valueOf cache
    pojo.setValue2(100000000000L);
    // avoid string intern pool cache
    pojo.setValue3(new String("abcdefghijklmnopqrstuvwxyz"));
    // create a new instance to void object output stream cache
    pojo.setValue4(new BigDecimal("12345678901234567890.12"));
    BitSet flags = pojo.getFlags();
    for (int i = 0; i < Constants.BIT_SET_SIZE; ++i) {
      if (i % 2 == 0) {
        flags.set(i);
      }
    }
  }

}

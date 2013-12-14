package com.github.marschall.serialization;

import static com.github.marschall.serialization.SerializationUtil.serializedSize;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static com.github.marschall.serialization.SerializationUtil.serializeJackson;

import org.junit.Test;

public class SizeTest {

  private static final int BIT_SET_SIZE = 70;

  @Test
  public void singleObject() throws IOException {
    ExternalizedPojo externalizedPojo = new ExternalizedPojo();
    initialize(externalizedPojo);
    SerializedPojo serializedPojo = new SerializedPojo();
    initialize(serializedPojo);

    int externalizedSize = serializedSize(externalizedPojo);
    int serializedSize = serializedSize(serializedPojo);
    int jsonSize = serializeJackson(serializedPojo).length;

    System.out.printf("externalized size:\t%, 7d%n", externalizedSize);
    System.out.printf("serialized size:\t%, 7d%n", serializedSize);
    System.out.printf("JSON size:\t\t%, 7d%n", jsonSize);
    System.out.printf("relative:\t\t%.2f %%%n", ((double) externalizedSize / (double) serializedSize) * 100.0d);
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
    int jsonSize = serializeJackson(serializedPojos).length;

    System.out.printf("externalized list size:\t%, 7d%n", externalizedSize);
    System.out.printf("serialized list size:\t%, 7d%n", serializedSize);
    System.out.printf("JSON size:\t\t%, 7d%n", jsonSize);
    System.out.printf("relative:\t\t%.2f %%%n", ((double) externalizedSize / (double) serializedSize) * 100.0d);
    System.out.printf("============================================%n");

  }

  private void initialize(WritablePojo externalizedPojo) {
    // avoid Integer.valueOf cache
    externalizedPojo.setValue1(1000);
    // avoid Long.valueOf cache
    externalizedPojo.setValue2(1000L);
    // avoid string intern pool cache
    externalizedPojo.setValue3(new String("abcdefghijklmnopqrstuvwxyz"));
    // create a new instance to void object output stream cache
    externalizedPojo.setValue4(new BigDecimal("12345678901234567890.12"));
    BitSet flags = new BitSet(BIT_SET_SIZE);
    for (int i = 0; i < BIT_SET_SIZE; ++i) {
      if (i % 2 == 0) {
        flags.set(i);
      }
    }
    externalizedPojo.setFlags(flags);
  }

}

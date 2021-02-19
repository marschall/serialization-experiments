package com.github.marschall.serialization.gto;

import static com.github.marschall.serialization.SerializationUtil.serializedGsonSize;
import static com.github.marschall.serialization.SerializationUtil.serializedJacksonSize;
import static com.github.marschall.serialization.SerializationUtil.serializedSize;
import static com.github.marschall.serialization.gto.PojoUtils.generatePojoList;
import static com.github.marschall.serialization.gto.PojoUtils.initialize;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.junit.jupiter.api.Test;

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
    int gsonSize = serializedGsonSize(serializedPojo);

    System.out.printf("single%n");
    System.out.printf("------%n");
    System.out.printf("externalized:\t%, 7d%n", externalizedSize);
    System.out.printf("serialized:\t%, 7d%n", serializedSize);
    System.out.printf("GSON:\t\t%, 7d%n", gsonSize);
    System.out.printf("Jackson:\t%, 7d%n", jsonSize);
    System.out.printf("relative:\t%.2f %%%n", ((double) externalizedSize / (double) serializedSize) * 100.0d);
    System.out.printf("============================================%n");
  }

  @Test
  public void multipleObjects() throws IOException {
    int arraySize = 100;
    List<ExternalizedPojo> externalizedPojos = generatePojoList(ExternalizedPojo.class, arraySize);
    List<SerializedPojo> serializedPojos = generatePojoList(SerializedPojo.class, arraySize);

    int externalizedSize = serializedSize((Serializable) externalizedPojos);
    int serializedSize = serializedSize((Serializable) serializedPojos);
    int jsonSize = serializedJacksonSize(serializedPojos);
    int gsonSize = serializedGsonSize(serializedPojos);

    System.out.printf("list%n");
    System.out.printf("----%n");
    System.out.printf("externalized:\t%, 7d%n", externalizedSize);
    System.out.printf("serialized:\t%, 7d%n", serializedSize);
    System.out.printf("GSON:\t\t%, 7d%n", gsonSize);
    System.out.printf("Jackson:\t%, 7d%n", jsonSize);
    System.out.printf("relative:\t%.2f %%%n", ((double) externalizedSize / (double) serializedSize) * 100.0d);
    System.out.printf("============================================%n");

  }

}

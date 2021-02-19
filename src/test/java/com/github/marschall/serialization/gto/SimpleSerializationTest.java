package com.github.marschall.serialization.gto;

import static com.github.marschall.serialization.SerializationUtil.dersialize;
import static com.github.marschall.serialization.SerializationUtil.serialize;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class SimpleSerializationTest {

  public static Collection<WritablePojo> data() {
    return List.of(new SerializedPojo(), new ExternalizedPojo());
  }

  @ParameterizedTest
  @MethodSource("data")
  public void simpleSerialization(WritablePojo pojo) throws IOException, ClassNotFoundException {
    byte[] data = serialize(pojo);
    WritablePojo readBack = (WritablePojo) dersialize(data);
    assertNotNull(readBack);
  }

}

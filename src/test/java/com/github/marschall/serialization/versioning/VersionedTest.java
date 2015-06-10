package com.github.marschall.serialization.versioning;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;

public class VersionedTest {

  private static final Path OBJECT_PATH = Paths.get("src", "test", "resources", "object.ser");

  @Test
//  @Ignore
  public void readOldVersion() throws ClassNotFoundException, IOException {
    Versioned versioned = readVersioned(OBJECT_PATH);
    assertEquals("parent", versioned.getName());
    assertEquals(Collections.singleton("string"), versioned.getStrings());
//    assertEquals(Collections.singletonList("string"), versioned.getStrings());
  }

  @Test
  public void writeCurrentVersion() throws ClassNotFoundException, IOException {
    Versioned versioned = new Versioned("parent");
    versioned.addString("string");

    Versioned copy = copy(versioned);
    assertEquals("parent", copy.getName());
    assertEquals(Collections.singleton("string"), copy.getStrings());
  }

  private static Versioned copy(Versioned versioned) throws IOException, ClassNotFoundException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      try (ObjectOutputStream stream = new ObjectOutputStream(bos)) {
        stream.writeObject(versioned);
      }
      try (ObjectInputStream stream =  new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
        return (Versioned) stream.readObject();
      }
    }
  }

  @Test
//  @Ignore
  public void writeOldVersion() throws IOException {
    Versioned versioned = new Versioned("parent");
    versioned.addString("string");

    writeVersioned(versioned, OBJECT_PATH);
  }

  private static void writeVersioned(Versioned versioned, Path path) throws IOException {
    try (ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
      stream.writeObject(versioned);
    }
  }

  private static Versioned readVersioned(Path path) throws IOException, ClassNotFoundException {
    try (ObjectInputStream stream =  new ObjectInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
      return (Versioned) stream.readObject();
    }
  }

}

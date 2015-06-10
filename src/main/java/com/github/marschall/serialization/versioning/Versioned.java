package com.github.marschall.serialization.versioning;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class Versioned extends Parent implements Serializable {

  private static final long serialVersionUID = 1L;

//  private ArrayList<String> strings = new ArrayList<>();
  private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("strings", ArrayList.class)};

  private transient HashSet<String> strings = new HashSet<>();

  public Versioned(String name) {
    super(name);
  }

  public void addString(String string) {
    strings.add(string);
  }

  public Collection<String> getStrings() {
//    return Collections.unmodifiableList(strings);
    return Collections.unmodifiableSet(strings);
  }

  private void writeObject(ObjectOutputStream out) throws IOException {
    PutField putFields = out.putFields();
    putFields.put("strings", new ArrayList<>(strings));
    out.writeFields();
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    GetField getField = in.readFields();
    Object fieldValue = (ArrayList<?>) getField.get("strings", null);
    if (fieldValue instanceof ArrayList) {
      ArrayList<?> stringList = (ArrayList<?>) fieldValue;
      this.strings = new HashSet<>((ArrayList<String>) stringList);
    } else {
      this.strings = new HashSet<>();
    }
  }


}

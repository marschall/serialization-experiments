package com.github.marschall.serialization.versioning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Versioned extends Parent implements Serializable {

  private static final long serialVersionUID = 1L;

  private ArrayList<String> stirngs = new ArrayList<>();

  public Versioned(String name) {
    super(name);
  }

  public void addString(String string) {
    stirngs.add(string);
  }

  public Collection<String> getStirngs() {
    return Collections.unmodifiableList(stirngs);
  }


}

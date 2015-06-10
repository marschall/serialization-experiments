package com.github.marschall.serialization.versioning;

public class Parent implements Old {

  private String name;

  public Parent(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}

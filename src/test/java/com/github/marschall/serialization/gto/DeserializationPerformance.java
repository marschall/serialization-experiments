package com.github.marschall.serialization.gto;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
public class DeserializationPerformance {
  
  @Setup
  public void init() {
    
  }

}

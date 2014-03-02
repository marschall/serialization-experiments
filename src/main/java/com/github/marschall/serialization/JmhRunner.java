package com.github.marschall.serialization;

import org.openjdk.jmh.Main;

public class JmhRunner {

  private static final String TEST = ".*" + SerializationPerformance.class.getSimpleName() + ".*"; 
  
  public static void main(String[] args) {
      String[] arguments = new String[]{TEST,
          "-i", "10",
//          "-r", 5000 + "ms",
//          "-t", "" + 1,
//          "-w", "1000",
            "-wi", "10",
//          "-wi", "3",
//          "-l",
//          "-v"
      };
      Main.main(arguments);
  }

}
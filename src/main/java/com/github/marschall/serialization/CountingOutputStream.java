package com.github.marschall.serialization;

import java.io.IOException;
import java.io.OutputStream;

final class CountingOutputStream extends OutputStream {

  private int count;

  CountingOutputStream() {
    this.count = 0;
  }

  int getCount() {
    return this.count;
  }

  @Override
  public void write(int b) throws IOException {
    this.count += 1;
  }

  @Override
  public void write(byte[] b) throws IOException {
    this.count += b.length;
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    if (b == null) {
      throw new NullPointerException();
    } else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
      throw new IndexOutOfBoundsException();
    }
    this.count += len;
  }

}
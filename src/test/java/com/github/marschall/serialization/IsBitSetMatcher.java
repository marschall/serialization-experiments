package com.github.marschall.serialization;

import java.util.BitSet;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

final class IsBitSetMatcher extends TypeSafeMatcher<BitSet> {

  private final int index;

  IsBitSetMatcher(int index) {
    this.index = index;
  }

  @Factory
  static Matcher<BitSet> isBitSet(int index) {
    return new IsBitSetMatcher(index);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("has bit set: ");
    description.appendValue(this.index);
  }

  @Override
  protected boolean matchesSafely(BitSet item) {
    return item.get(this.index);
  }

}

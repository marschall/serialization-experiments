package com.github.marschall.serialization.context;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public final class OnlineProcessingDateTO extends AbstractTransferObject implements IOnlineProcessingDateProvider {

  private final Date processingDate;
  private final Date previousProcessingDate;
  private final Date nextProcessingDate;

  private final int hashCode;

  public OnlineProcessingDateTO(Date processingDate, Date previousProcessingDate, Date nextProcessingDate) {
    this.processingDate = processingDate;
    this.previousProcessingDate = previousProcessingDate;
    this.nextProcessingDate = nextProcessingDate;

    // All fields are final, calculate hash code now
    hashCode = new HashCodeBuilder(93, 49).
        append(processingDate).
        append(previousProcessingDate).
        append(nextProcessingDate).
        toHashCode();
  }


  public Date getProcessingDate() {
    return processingDate;
  }

  public Date getPreviousProcessingDate() {
    return previousProcessingDate;
  }

  public Date getNextProcessingDate() {
    return nextProcessingDate;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof OnlineProcessingDateTO)) {
      return false;
    }

    OnlineProcessingDateTO rhs = (OnlineProcessingDateTO) obj;

    if (this.hashCode != rhs.hashCode) {
      return false;
    }

    return new EqualsBuilder()
        .append(processingDate, rhs.processingDate)
        .append(previousProcessingDate, rhs.previousProcessingDate)
        .append(nextProcessingDate, rhs.nextProcessingDate)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return hashCode;
  }
}

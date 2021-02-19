package com.github.marschall.serialization.context;

import java.util.Locale;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class DefaultContextTO extends AbstractTransferObject {

  private final Long sessionId;
  private final Locale locale;
  private final Long institutionId;
  private final int hashCode;

  private final OnlineProcessingDateTO processingDateTO;

  public DefaultContextTO(Long sessionId, Locale locale, Long institutionId, OnlineProcessingDateTO processingDateTO) {
    this.sessionId = sessionId;
    this.locale = locale;
    this.institutionId = institutionId;
    this.processingDateTO = processingDateTO;

    this.hashCode = new HashCodeBuilder()
      .append(sessionId)
      .append(locale)
      .append(institutionId)
      .append(processingDateTO)
      .hashCode();

  }

  public OnlineProcessingDateTO getProcessingDateTO() {
    return this.processingDateTO;
  }

  public Long getSessionId() {
    return this.sessionId;
  }

  public Locale getLocale() {
    return this.locale;
  }

  public Long getInstitutionId() {
    return this.institutionId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof DefaultContextTO)) {
      return false;
    }
    DefaultContextTO other = (DefaultContextTO) obj;

    if (this.hashCode != other.hashCode) {
      return false;
    }

    return new EqualsBuilder()
      .append(this.sessionId, other.sessionId)
      .append(this.locale, other.locale)
      .append(this.institutionId, other.institutionId)
      .append(this.processingDateTO, other.processingDateTO)
      .isEquals();
  }

  @Override
  public int hashCode() {
    return this.hashCode;
  }

}

package com.github.marschall.serialization.context;

import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public final class OptimizedContextTO extends AbstractTransferObject {

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

    hashCode = new HashCodeBuilder()
      .append(sessionId)
      .append(locale)
      .append(institutionId)
      .append(processingDateTO)
      .hashCode();

  }

  public OnlineProcessingDateTO getProcessingDateTO() {
    return processingDateTO;
  }

  public Long getSessionId() {
    return sessionId;
  }

  public Locale getLocale() {
    return locale;
  }

  public Long getInstitutionId() {
    return institutionId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof OptimizedContextTO)) {
      return false;
    }
    OptimizedContextTO other = (OptimizedContextTO) obj;

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


  private Object writeReplace() {
      return new ContextTOSerializationProxy(this);
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

}

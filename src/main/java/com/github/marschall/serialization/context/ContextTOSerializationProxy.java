package com.github.marschall.serialization.context;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.Locale;

final class ContextTOSerializationProxy implements Externalizable {

  private OptimizedContextTO contextTO;

  public ContextTOSerializationProxy() {
    super();
  }

  ContextTOSerializationProxy(OptimizedContextTO contextTO) {
    this.contextTO = contextTO;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    Long sessionId = this.contextTO.getSessionId();
    writeLong(sessionId, out);

    Locale locale = this.contextTO.getLocale();
    out.writeUTF(locale.getLanguage());
    out.writeUTF(locale.getCountry());

    Long institutionId = this.contextTO.getInstitutionId();
    writeLong(institutionId, out);

    OnlineProcessingDateTO processingDateTO = this.contextTO.getProcessingDateTO();
    if (processingDateTO != null) {
      writeDate(processingDateTO.getPreviousProcessingDate(), out);
      writeDate(processingDateTO.getProcessingDate(), out);
      writeDate(processingDateTO.getNextProcessingDate(), out);
    } else {
      writeLong(null, out);
      writeLong(null, out);
      writeLong(null, out);
    }
  }


  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    Long sessionId = readLong(in);

    String language = in.readUTF();
    String country = in.readUTF();
    Locale locale = new Locale(language, country);

    Long institutionId = readLong(in);

    Date previousProcessingDate = readDate(in);
    Date processingDate = readDate(in);
    Date nextProcessingDate = readDate(in);

    OnlineProcessingDateTO processingDateTO;
    if (previousProcessingDate == null && processingDate == null && nextProcessingDate == null) {
      processingDateTO = null;
    } else {
      processingDateTO = new OnlineProcessingDateTO(processingDate, previousProcessingDate, nextProcessingDate);
    }
    this.contextTO = new OptimizedContextTO(sessionId, locale, institutionId, processingDateTO);
  }

  private static Date readDate(ObjectInput in) throws IOException {
    long time = in.readLong();
    if (time == Long.MIN_VALUE) {
      return null;
    } else {
      return new Date(time);
    }
  }

  private static void writeDate(Date date, ObjectOutput out) throws IOException {
    if (date != null) {
      out.writeLong(date.getTime());
    } else {
      out.writeLong(Long.MIN_VALUE);
    }
  }

  private static void writeLong(Long l, ObjectOutput out) throws IOException {
    if (l != null) {
      out.writeLong(l);
    } else {
      out.writeLong(Long.MIN_VALUE);
    }
  }

  private static Long readLong(ObjectInput in) throws IOException {
    long value = in.readLong();
    if (value == Long.MIN_VALUE) {
      return null;
    } else {
      return value;
    }
  }


  private Object readResolve() {
    return contextTO;
  }

}
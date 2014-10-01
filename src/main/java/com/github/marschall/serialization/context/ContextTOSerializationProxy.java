package com.github.marschall.serialization.context;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidObjectException;
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
    if (institutionId != null) {
      long value = institutionId;
      if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
        throw new InvalidObjectException("institution id too large");
      }
      writeInteger((int) value, out);
    } else {
      writeNullInteger(null, out);
    }

    OnlineProcessingDateTO processingDateTO = this.contextTO.getProcessingDateTO();
    if (processingDateTO != null) {
      writeDate(processingDateTO.getPreviousProcessingDate(), out);
      writeDate(processingDateTO.getProcessingDate(), out);
      writeDate(processingDateTO.getNextProcessingDate(), out);
    } else {
      writeDate(null, out);
      writeDate(null, out);
      writeDate(null, out);
    }
  }


  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    Long sessionId = readLong(in);

    String language = in.readUTF();
    String country = in.readUTF();
    Locale locale = new Locale(language, country);

    Long institutionId = readIntegerIntoLong(in);

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
    int time = in.readInt();
    if (time == Integer.MIN_VALUE) {
      return null;
    } else {
      return new Date(time * 1000L);
    }
  }

  private static void writeDate(Date date, ObjectOutput out) throws IOException {
    if (date != null) {
      out.writeInt((int) (date.getTime() / 1000L));
    } else {
      out.writeInt(Integer.MIN_VALUE);
    }
  }

  private static void writeInteger(int i, ObjectOutput out) throws IOException {
    out.writeInt(i);
  }
  
  private static void writeNullInteger(Integer i, ObjectOutput out) throws IOException {
    out.writeInt(Integer.MIN_VALUE);
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

  private static Long readIntegerIntoLong(ObjectInput in) throws IOException {
    int value = in.readInt();
    if (value == Long.MIN_VALUE) {
      return null;
    } else {
      return Long.valueOf(value);
    }
  }
  


  private Object readResolve() {
    return contextTO;
  }

}

package com.github.marschall.serialization.context;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

final class ContextTOBuilder {
  
  private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

  
  private static Date today() {
    return toDate(LocalDate.now());
  }
  
  private static Date yesterday() {
    return toDate(LocalDate.now().minusDays(1L));
  }
  
  private static Date tomorrow() {
    return toDate(LocalDate.now().plusDays(1L));
  }


  public static Date toDate(LocalDate now) {
    return new Date(now.atStartOfDay(PARIS).toInstant().toEpochMilli());
  }
  
}

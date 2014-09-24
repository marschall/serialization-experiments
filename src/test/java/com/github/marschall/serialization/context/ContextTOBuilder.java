package com.github.marschall.serialization.context;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

final class ContextTOBuilder {
  
  private static final ZoneId PARIS = ZoneId.of("Europe/Paris");
  
  static DefaultContextTO newDefaultContextTO() {
    return new DefaultContextTO(42L, Locale.GERMANY, 42L, newOnlineProcessingDateTO());
  }
  
  static OptimizedContextTO newOptimizedContextTO() {
    return new OptimizedContextTO(42L, Locale.GERMANY, 42L, newOnlineProcessingDateTO());
  }
  
  private static OnlineProcessingDateTO newOnlineProcessingDateTO() {
    return new OnlineProcessingDateTO(today(), yesterday(), tomorrow());
  }


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

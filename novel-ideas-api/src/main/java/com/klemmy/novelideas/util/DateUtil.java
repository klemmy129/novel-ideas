package com.klemmy.novelideas.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateUtil {

  private DateUtil() {
  }

  public static Instant zonedDateTimeToInstant(ZonedDateTime dtz) {
    return dtz != null ? dtz.toInstant() : null;
  }

  public static LocalDateTime instantToLocalDateTimeUTC(Instant instant) {
    return instant != null ? instant.atZone(ZoneOffset.UTC).toLocalDateTime() : null;
  }

  public static ZonedDateTime instantToZonedDateTimeUTC(Instant instant) {
    return instant != null ? instant.atZone(ZoneOffset.UTC) : null;
  }

}

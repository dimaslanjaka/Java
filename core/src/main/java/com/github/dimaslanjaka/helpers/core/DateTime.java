package com.dimaslanjaka.tools.Helpers.core;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DateTime {
  public static final List<Long> times = Arrays.asList(
          TimeUnit.DAYS.toMillis(365),
          TimeUnit.DAYS.toMillis(30),
          TimeUnit.DAYS.toMillis(1),
          TimeUnit.HOURS.toMillis(1),
          TimeUnit.MINUTES.toMillis(1),
          TimeUnit.SECONDS.toMillis(1));
  public static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");
  public static final long ONE_MINUTE = 60 * 1000;
  public static final long ONE_HOUR = 60 * ONE_MINUTE;
  public static final long ONE_DAY = 24 * ONE_HOUR;

  public static boolean isYesterday(Date d) {
    return DateUtils.isToday(d.getTime() + ONE_DAY);
  }

  public static boolean isMoreThan(Date now, Date ago, int minutes) {
    long milliseconds = now.getTime() - ago.getTime();
    return (ONE_MINUTE * minutes) > milliseconds;
  }

  /**
   * Return date in specified format.
   *
   * @param milliSeconds Date in milliseconds
   * @param dateFormat   Date format
   * @return String representing date in specified format
   */
  public static String getDate(long milliSeconds, String dateFormat) {
    // Create a DateFormatter object for displaying date in specified format.
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(milliSeconds);
    return formatter.format(calendar.getTime());
  }

  /**
   * Calculate time ago from date millis (long)
   *
   * @param duration time milliseconds
   * @return String representing time ago
   */
  public static String TimeAgo(long duration) {
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < DateTime.times.size(); i++) {
      Long current = DateTime.times.get(i);
      long temp = duration / current;
      if (temp > 0) {
        res.append(temp).append(" ").append(DateTime.timesString.get(i)).append(temp != 1 ? "s" : "").append(" ago");
        break;
      }
    }
    if ("".equals(res.toString())) {
      return "0 seconds ago";
    } else {
      return res.toString().trim().toLowerCase();
    }
  }

  public static void example(String[] args) {
    System.out.println(TimeAgo(123));
    System.out.println(TimeAgo(1230));
    System.out.println(TimeAgo(12300));
    System.out.println(TimeAgo(123000));
    System.out.println(TimeAgo(1230000));
    System.out.println(TimeAgo(12300000));
    System.out.println(TimeAgo(123000000));
    System.out.println(TimeAgo(1230000000));
    System.out.println(TimeAgo(12300000000L));
    System.out.println(TimeAgo(123000000000L));
  }
}
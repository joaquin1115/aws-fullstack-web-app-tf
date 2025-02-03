package com.sanfernando.sanfernando.utils;

import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeUtils {
  private Time time;
  private static final DecimalFormat decimalFormat = new DecimalFormat("#.00");

  public double convertTimeToHours(Time time) {
    try {
      int hours = time.toLocalTime().getHour();
      int minutes = time.toLocalTime().getMinute();
      double response = hours + (minutes / 60.0);
      return Double.parseDouble(decimalFormat.format(response));
    }catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public String localTimePlus(int seconds) {
    LocalTime time = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    time = time.plusSeconds(seconds);
    String timeFormatted = time.format(formatter);
    return timeFormatted;
  }

  public String getCurrentDate() {
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return currentDate.format(dateFormatter);
  }

  public String getCurrentTime() {
    LocalTime currentTime = LocalTime.now();
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    return currentTime.format(timeFormatter);
  }
}

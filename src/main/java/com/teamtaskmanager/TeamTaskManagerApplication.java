package com.teamtaskmanager;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeamTaskManagerApplication {
  public static void main(String[] args) {
    setApplicationTimeZone();
    SpringApplication.run(TeamTaskManagerApplication.class, args);
  }

  private static void setApplicationTimeZone() {
    String configuredTimeZone = System.getenv().getOrDefault("APP_TIME_ZONE", "Asia/Kolkata");
    try {
      TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(configuredTimeZone)));
    } catch (DateTimeException ignored) {
      TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }
  }
}

package com.goga74.platform.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;

public class TimeConverter {

    public static String convertTimestamp(int timestamp, int timezone) {
        // Создаем Instant из timestamp
        Instant instant = Instant.ofEpochSecond(timestamp);

        // Создаем ZoneId с учетом переданного смещения (timezone)
        ZoneId zoneId = ZoneId.of("GMT+" + timezone);

        // Преобразуем Instant в ZonedDateTime с учетом временной зоны
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);

        // Форматируем дату и время в нужный формат
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

        // Возвращаем отформатированную строку
        return zonedDateTime.format(formatter);
    }

    public static String getTimeInTimezone(int timestamp, int timezone) {
        // Создаем Instant из timestamp
        Instant instant = Instant.ofEpochSecond(timestamp);

        // Создаем ZoneId с учетом переданного смещения (например, GMT+2)
        ZoneId zoneId = ZoneId.of("GMT+" + timezone);

        // Преобразуем Instant в ZonedDateTime с учетом временной зоны
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);

        // Форматируем ZonedDateTime в строку (например, "2025-07-23T17:00:00+02:00")
        String dateTimeString = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        // Создаем Instant из строки
        Instant parsedInstant = Instant.parse(dateTimeString);

        // Получаем правила временной зоны
        ZoneRules zoneRules = zoneId.getRules();

        // Проверяем, действует ли летнее время для данного момента (parsedInstant)
        boolean isDaylightSavings = zoneRules.isDaylightSavings(parsedInstant);

        // Если действует летнее время, добавляем 1 час
        if (isDaylightSavings) {
            zonedDateTime = zonedDateTime.plusHours(1);
        }

        // Форматируем время в формат "HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Возвращаем отформатированное время
        return zonedDateTime.format(formatter);
    }

    public static String getTimeInTallinn(int timestamp) {
        // Создаем Instant из timestamp
        Instant instant = Instant.ofEpochSecond(timestamp);

        // Создаем ZoneId для Эстонии (Europe/Tallinn)
        ZoneId zoneId = ZoneId.of("Europe/Tallinn");

        // Преобразуем Instant в ZonedDateTime с учетом временной зоны
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);

        // Форматируем время в формат "HH:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Возвращаем отформатированное время
        return zonedDateTime.format(formatter);
    }


    public static void main(String[] args) {
        int timestamp = 1742565600;
        int timezone = 2; // GMT+2
        String result = convertTimestamp(timestamp, timezone);
        System.out.println(result); // Вывод: 25 Jul 2025 17:00:00

        // Получаем время в формате "HH:mm" с учетом временной зоны
        String result2 = getTimeInTimezone(timestamp, timezone);
        System.out.println(result2); // Вывод: 17:00 (если timestamp в летнем времени)

        List<Integer> timestamps = Arrays.asList(
                1742565600, // 21 March 2025 14:00 UTC
                1742757000, // Sunday, 23 March 2025 19:10
                1753282800 // 23 July 2025 г., 15:00:00
        );

        // Вычисляем время для каждого timestamp
        for (int timestamp2 : timestamps) {
            String time = getTimeInTallinn(timestamp2);
            System.out.println("Timestamp: " + timestamp2 + " -> Time: " + time);
        }

        int timestamp3 = 1742565600; // Пример timestamp
        Instant instant = Instant.ofEpochSecond(timestamp3);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
        System.out.println("Timestamp: " + timestamp3 + " -> Date: " + zonedDateTime);

        System.out.println("Current zone: " + ZoneId.systemDefault());

        int timestamp4 = 1753282800; // 23 июля 2025 года, 15:00:00 UTC
        int timezone4 = 2; // GMT+2

        // Вычисляем время
        String time = getTimeInTimezone(timestamp4, timezone4);
        System.out.println("Timestamp: " + timestamp4 + " -> Time: " + time);
    }

}

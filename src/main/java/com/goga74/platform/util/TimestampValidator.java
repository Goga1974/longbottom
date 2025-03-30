package com.goga74.platform.util;

public class TimestampValidator {

    /**
     * Проверяет, является ли строка валидным Unix timestamp.
     * @param timestamp строка для проверки
     * @return true, если timestamp валиден, иначе false
     */
    public static boolean isValid(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return false; // Пустая или null строка невалидна
        }

        try {
            long unixTimestamp = Long.parseLong(timestamp);

            // Проверяем, что timestamp находится в допустимом диапазоне
            // Unix time начинается с 1 января 1970 года (0) и не может быть отрицательным
            if (unixTimestamp < 0) {
                return false;
            }

            // Дополнительно можно проверить, что timestamp не превышает текущего времени
            long currentUnixTime = System.currentTimeMillis() / 1000;
            return unixTimestamp <= currentUnixTime;
        } catch (NumberFormatException e) {
            return false; // Если строка не может быть преобразована в число, она невалидна
        }
    }
}

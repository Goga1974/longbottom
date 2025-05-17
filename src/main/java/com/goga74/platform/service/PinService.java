package com.goga74.platform.service;
import java.util.Random;

public class PinService
{
    private static final Random random = new Random();

    public static String generatePin()
    {
        StringBuilder pin = new StringBuilder(4);
        for (int i = 0; i < 4; i++)
        {
            pin.append(random.nextInt(10)); // Генерация случайной цифры от 0 до 9
        }
        return pin.toString();
    }

}

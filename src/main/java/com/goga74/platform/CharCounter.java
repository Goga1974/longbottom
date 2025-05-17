package com.goga74.platform;

/* TASK: write a java method that accepts a string and returns a map
 where the key is a letter in the string and the value is the number
 of times it appears in the string.
 include spaces and any punctuation as well. the example string is "Hello World!"
 */

import java.util.HashMap;

public class CharCounter
{

    public static void main(String[] args)
    {
        final String test = "Hello World!";
        HashMap<Character, Integer> map = Test(test);
        System.out.println(map);
    }

    public static HashMap<Character, Integer> Test(final String input)
    {
        // amount of keys is about 46
        HashMap<Character, Integer> map = new HashMap<>();

        if (input != null)
        {
            for (Character c : input.toCharArray())
            {
                Integer counter = map.getOrDefault(c, 0);
                map.put(c, ++counter);
            }
        }
        return map;
    }

}

package com.goga74.platform.util;

import com.goga74.platform.DB.entity.ItemEntity;
import com.goga74.platform.dto.Item;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class JsonUtil
{
    private static final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public static String convertToJson(List<Item> items)
    {
        return gson.toJson(items);
    }

    public static List<ItemEntity> convertFromJson(String json)
    {
        Type itemListType = new TypeToken<List<Item>>() {}.getType();
        return gson.fromJson(json, itemListType);
    }

    public static <T> T convertFromJson(String json, Class<T> clazz)
    {
        return gson.fromJson(json, clazz);
    }

    public static <T> T convertFromJson(String json, Type typeOfT)
    {
        return gson.fromJson(json, typeOfT);
    }

}
package com.dimaslanjaka.library.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

public class Var {
    public static Type typeListHttpCookie = new TypeToken<List<HttpCookie>>() {
    }.getType();
    public static Type typeMapStrStr = new TypeToken<Map<String, String>>() {
    }.getType();
    public static Gson gson = new GsonBuilder().create();

    /**
     * @param o Object Variable
     * @return Type of variable
     * @see <a href="https://stackoverflow.com/a/26507980">https://stackoverflow.com/a/26507980</a>
     */
    public static <T> String nameOf(T o) {
        return o.getClass().getSimpleName();
    }

    /**
     * @param o Object Variable
     * @return Type of variable
     * @see <a href="https://stackoverflow.com/a/26507980">https://stackoverflow.com/a/26507980</a>
     */
    private static String varNameOf(Object o) {
        return o.getClass().getSimpleName();
    }

    /**
     * Transform json string to object
     *
     * @param json json string
     * @param type java reflect type
     * @return object can be transformed by (TypeObject)
     */
    public static Object fromJson(String json, Type type) {
        return gson.fromJson(json, type);   // convert json string to list
    }

    public static Object fromJson(String json, Class clazz) {
        return gson.fromJson(json, clazz);
    }
}

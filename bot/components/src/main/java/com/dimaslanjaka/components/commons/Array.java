package com.dimaslanjaka.components.commons;

import java.util.Random;

public class Array {
    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public static long getRandom(long[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public static Double getRandom(Double[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public static Object getRandom(Object[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
}

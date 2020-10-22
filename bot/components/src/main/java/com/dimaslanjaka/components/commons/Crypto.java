package com.dimaslanjaka.components.commons;

import com.dimaslanjaka.components.org.apache.commons.codec.digest.DigestUtils;

public class Crypto {
    public static String md5(String text) {
        return DigestUtils.md5Hex(text);
    }
}

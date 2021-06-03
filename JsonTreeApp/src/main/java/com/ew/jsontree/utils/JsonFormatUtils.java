package com.ew.jsontree.utils;

import com.alibaba.fastjson.JSON;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by WYM on 2016/7/11.
 */
public class JsonFormatUtils {

    public static final String TAB = "  ";

    public static String formatJson(String json) {
        if (json != null && json.length() > 0) {
            StringBuilder sb = new StringBuilder();
            int tabCount = 0;
            for (int i = 0; i < json.length(); i++) {
                switch (json.charAt(i)) {
                    case '{':
                    case '[':
                        sb.append(json.charAt(i)).append("\n");
                        printTAB(++tabCount, sb);
                        break;
                    case '}':
                    case ']':
                        sb.append("\n");
                        printTAB(tabCount--, sb);
                        sb.append(json.charAt(i));
                        if (json.length() < i + 1) {
                            if (json.charAt(i + 1) != ',') {
                                sb.append("\n");
                            }
                        }
                        break;
                    case ',':
                        sb.append(json.charAt(i)).append("\n");
                        printTAB(tabCount, sb);
                        break;
                    default:
                        sb.append(json.charAt(i));
                        break;
                }
            }
            return sb.toString();
        }
        return "";
    }

    public static void printTAB(int count, StringBuilder sb) {
        while (count-- > 0) {
            sb.append(TAB);
        }
    }

    /**
     * json string转换为LinkedHashMap，map中节点顺序保持输入顺序
     *
     * @param json
     * @return
     */
    public static LinkedHashMap<String, Object> jsonToMapKeeped(String json) {
        JSONObjectKeeped jsonObjectKeeped = null;
        try {
            jsonObjectKeeped = new JSONObjectKeeped(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObjectKeeped == null) {
            return null;
        }
        return jsonObjectKeeped.getMap();
    }

    /**
     * json string转化为Map，适用于fastJson，不保持节点输入顺序
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json) {
        Map<String, Object> map = (Map<String, Object>) JSON.parse(json);
        return map;
    }

    /**
     * json string 转换为LinkedHashMap，适用于fastJson,只有最外层节点保持输入顺序
     *
     * @param json
     * @return
     */
    public static LinkedHashMap<String, Object> jsonToMapOfOrdered(String json) {
        LinkedHashMap<String, Object> map = JSON.parseObject(json, new LinkedHashMap<String, Object>().getClass());
        return map;
    }

    /**
     * json string转化为rog.json.JSONObject
     * 需要android-20及以上采用的是LinkedHashMap保持输入顺序，以下则采用Map不保持顺序
     *
     * @param json
     * @return
     */
    public static JSONObject jsonToObjectOrdered(String json) {
        try {
            JSONObject object = new JSONObject(json);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.ew.jsontree.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONStringer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 保持json节点输入顺序的工具类
 * Created by WYM on 2016/7/25.
 */
public class JSONObjectKeeped {

    public static final Object NULL = new Object() {
        @Override
        public boolean equals(Object o) {
            return o == this || o == null; // API specifies this broken equals implementation
        }

        @Override
        public String toString() {
            return "null";
        }
    };

    private final LinkedHashMap<String, Object> nameValuePairs;


    public JSONObjectKeeped() {
        nameValuePairs = new LinkedHashMap<String, Object>();
    }

    public JSONObjectKeeped(JSONTokenerKeeped readFrom) throws JSONException {

        Object object = readFrom.nextValue();
        if (object instanceof JSONObjectKeeped) {
            this.nameValuePairs = ((JSONObjectKeeped) object).nameValuePairs;
        } else {
            throw typeMismatch(object, "JSONObject");
        }
    }

    public JSONObjectKeeped(String json) throws JSONException {
        this(new JSONTokenerKeeped(json));
    }


    public LinkedHashMap<String, Object> getMap() {
        return nameValuePairs;
    }

    public int length() {
        return nameValuePairs.size();
    }

    /**
     * Maps {@code name} to {@code value}, clobbering any existing name/value
     * mapping with the same name.
     *
     * @return this object.
     */
    public JSONObjectKeeped put(String name, boolean value) throws JSONException {
        nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * Maps {@code name} to {@code value}, clobbering any existing name/value
     * mapping with the same name.
     *
     * @return this object.
     */
    public JSONObjectKeeped put(String name, int value) throws JSONException {
        nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * Maps {@code name} to {@code value}, clobbering any existing name/value
     * mapping with the same name.
     *
     * @param value a finite value. May not be {@link Double#isNaN() NaNs} or
     *              {@link Double#isInfinite() infinities}.
     * @return this object.
     */
    public JSONObjectKeeped put(String name, double value) throws JSONException {
        nameValuePairs.put(checkName(name), checkDouble(value));
        return this;
    }

    /**
     * Maps {@code name} to {@code value}, clobbering any existing name/value
     * mapping with the same name.
     *
     * @return this object.
     */
    public JSONObjectKeeped put(String name, long value) throws JSONException {
        nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * Maps {@code name} to {@code value}, clobbering any existing name/value
     * mapping with the same name. If the value is {@code null}, any existing
     * mapping for {@code name} is removed.
     *
     * @param value a {@link JSONObjectKeeped}, {@link JSONArray}, String, Boolean,
     *              Integer, Long, Double, {@link #NULL}, or {@code null}. May not be
     *              {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
     *              infinities}.
     * @return this object.
     */
    public JSONObjectKeeped put(String name, Object value) throws JSONException {
        if (value == null) {
            nameValuePairs.remove(name);
            return this;
        }
        if (value instanceof Number) {
            // deviate from the original by checking all Numbers, not just floats & doubles
            checkDouble(((Number) value).doubleValue());
        }
        nameValuePairs.put(checkName(name), value);
        return this;
    }

    /**
     * Appends values to the array mapped to {@code name}. A new {@link JSONArray}
     * mapping for {@code name} will be inserted if no mapping exists. If the existing
     * mapping for {@code name} is not a {@link JSONArray}, a {@link JSONException}
     * will be thrown.
     *
     * @throws JSONException if {@code name} is {@code null} or if the mapping for
     *                       {@code name} is non-null and is not a {@link JSONArray}.
     * @hide
     */
    public JSONObjectKeeped append(String name, Object value) throws JSONException {
        Object current = nameValuePairs.get(checkName(name));

        final JSONArray array;
        if (current instanceof JSONArray) {
            array = (JSONArray) current;
        } else if (current == null) {
            JSONArray newArray = new JSONArray();
            nameValuePairs.put(name, newArray);
            array = newArray;
        } else {
            throw new JSONException("Key " + name + " is not a JSONArray");
        }

        checkedPut(array, value);

        return this;
    }

    String checkName(String name) throws JSONException {
        if (name == null) {
            throw new JSONException("Names must be non-null");
        }
        return name;
    }

    /**
     * Removes the named mapping if it exists; does nothing otherwise.
     *
     * @return the value previously mapped by {@code name}, or null if there was
     * no such mapping.
     */
    public Object remove(String name) {
        return nameValuePairs.remove(name);
    }

    /**
     * Returns true if this object has a mapping for {@code name}. The mapping
     * may be {@link #NULL}.
     */
    public boolean has(String name) {
        return nameValuePairs.containsKey(name);
    }

    /**
     * Encodes this object as a compact JSON string, such as:
     * <pre>{"query":"Pizza","locations":[94043,90210]}</pre>
     */
    @Override
    public String toString() {
        try {
            JSONStringer stringer = new JSONStringer();
            writeTo(stringer);
            return stringer.toString();
        } catch (JSONException e) {
            return null;
        }
    }

    void writeTo(JSONStringer stringer) throws JSONException {
        stringer.object();
        for (Map.Entry<String, Object> entry : nameValuePairs.entrySet()) {
            stringer.key(entry.getKey()).value(entry.getValue());
        }
        stringer.endObject();
    }

    public double checkDouble(double d) throws JSONException {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new JSONException("Forbidden numeric value: " + d);
        }
        return d;
    }

    public void checkedPut(JSONArray jsonArray, Object value) throws JSONException {
        if (value instanceof Number) {
            checkDouble(((Number) value).doubleValue());
        }

        jsonArray.put(value);
    }

    public JSONException typeMismatch(Object actual, String requiredType)
            throws JSONException {
        if (actual == null) {
            throw new JSONException("Value is null.");
        } else {
            throw new JSONException("Value " + actual
                    + " of type " + actual.getClass().getName()
                    + " cannot be converted to " + requiredType);
        }
    }

    public String toString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }

}

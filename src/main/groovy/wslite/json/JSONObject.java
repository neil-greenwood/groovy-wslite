package wslite.json;

import org.json.*;
import java.util.*;

public class JSONObject implements Map<String, Object> {

    private static final class Null {

        protected final Object clone() {
            return this;
        }

        public boolean equals(Object object) {
            return object == null || object == this;
        }

        public String toString() {
            return "null";
        }

        public boolean asBoolean() {
            return false;
        }

        public Object propertyMissing(String name) {
            return null;
        }
    }

    public static final Object NULL = new Null();

    private org.json.JSONObject wrapped;

    public JSONObject() {
        wrapped = new org.json.JSONObject();
    }

    protected JSONObject(org.json.JSONObject jo) {
        wrapped = jo;
    }

    public JSONObject(Map map) {
        wrapped = new org.json.JSONObject(wrapMap(map));
    }

    public JSONObject(Object bean) {
        wrapped = new org.json.JSONObject(wrap(bean));
    }

    public JSONObject(String source) throws JSONException {
        wrapped = new org.json.JSONObject(source);
    }

    public JSONObject(String baseName, Locale locale) throws JSONException {
        wrapped = new org.json.JSONObject(baseName, locale);
    }

    public int size() {
        return wrapped.length();
    }

    public boolean isEmpty() {
        return wrapped.length() == 0;
    }

    public boolean containsKey(Object key) {
        return (key != null) ? wrapped.has(key.toString()) : false;
    }

    public boolean containsValue(Object value) {
        //TODO: we should probably use NULL object instead if value == null
        for (Iterator<String> i = wrapped.keys(); i.hasNext(); ) {
            Object v = get(i.next());
            if (value == null && v == null) {
                return true;
            }
            if (value != null) {
                if (value instanceof CharSequence) {
                    if (value.toString().equals(v)) {
                        return true;
                    }
                } else {
                    if (value.equals(v)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Object get(Object key) {
        if (key == null) {
            return key;
        }
        Object o = wrapped.opt(key.toString());
        return wrap(o);
    }

    public Object put(String key, Object value) {
        if (key == null) {
            throw new NullPointerException("Null keys are not allowed.");
        }
        Object o = get(key);
        wrapped.put(key.toString(), wrap(value));
        return o;
    }

    public Object remove(Object key) {
        return wrapped.remove(key.toString());
    }

    public void putAll(Map m) {
        for (Object o : m.keySet()) {
            put((String) o, wrap(m.get(o)));
        }
    }

    public void clear() {
        for (Iterator keys = wrapped.keys(); keys.hasNext(); ) {
            remove(keys.next());
        }
    }

    public Set keySet() {
        return wrapped.keySet();
    }

    public Collection values() {
        throw new UnsupportedOperationException();
    }

    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> entrySet = new HashSet<Entry<String, Object>>(wrapped.length());
        for (Object key : wrapped.keySet()) {
            Object value = wrapped.get(key.toString());
            Map.Entry entry = new JSONEntry(key.toString(), value);
            entrySet.add(entry);
        }
        return entrySet;
    }

    static Object wrap(Object o) {
        if (o instanceof wslite.json.JSONArray) {
            return o;
        }
        if (o instanceof wslite.json.JSONObject) {
            return o;
        }
        if (o instanceof CharSequence) {
            return (o != null) ? o.toString() : null;
        }
        if (o instanceof Map) {
            return wrapMap((Map) o);
        }
        if (o instanceof org.json.JSONObject) {
            return wrapJSONObject((org.json.JSONObject) o);
        }
        if (o != null && o == org.json.JSONObject.NULL) {
            return NULL;
        }
        if (o instanceof org.json.JSONArray) {
            return new wslite.json.JSONArray((org.json.JSONArray) o);
        }
        return o;
    }

    static JSONObject wrapJSONObject(org.json.JSONObject o) {
        return new JSONObject(o);
    }

    static Map wrapMap(final Map map) {
        Map m = new HashMap(map);
        Set keys = new HashSet(m.keySet());
        for (Object k : keys) {
            Object v = m.get(k);
            if (k instanceof CharSequence) {
                m.remove(k);
                m.put(k.toString(), v);
            }
            if (v instanceof Map) {
                m.put(k.toString(), wrapMap((Map) v));
            }
            if (v instanceof CharSequence) {
                m.put(k.toString(), v.toString());
            }
        }
        return m;
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }

    @Override
    public boolean equals(Object o) {
        return wrapped.equals(o);
    }

    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }

    private class JSONEntry implements Entry {

        private final Object key;
        private Object value;

        private JSONEntry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object value) {
            this.value = value;
            return put(key.toString(), wrap(value));
        }
    }
}

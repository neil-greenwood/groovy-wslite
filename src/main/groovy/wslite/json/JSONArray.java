package wslite.json;

import org.json.*;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class JSONArray implements Collection {

    private org.json.JSONArray wrapped;

    public JSONArray() {
        wrapped = new org.json.JSONArray();
    }

    public JSONArray(JSONTokener x) {
        wrapped = new org.json.JSONArray(x);
    }

    public JSONArray(String source) throws org.json.JSONException {
        wrapped = new org.json.JSONArray(source);
    }

    public JSONArray(Collection collection) {
        wrapped = new org.json.JSONArray(wrap(collection));
    }

    public JSONArray(Object array) throws JSONException {
        wrapped = new org.json.JSONArray(array);
    }

    public int size() {
        return wrapped.length();
    }

    public boolean isEmpty() {
        return wrapped.length() == 0;
    }

    public boolean contains(Object o) {
        for (int i = 0; i < wrapped.length(); i++) {
            Object v = wrapped.opt(i);
            if (o == null) {
                if (v == null) {
                    return true;
                }
            } else {
                if (o.equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Iterator iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object[] toArray() {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean add(Object o) {
        return wrapped.put(o) != null;
    }

    public boolean remove(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean containsAll(Collection c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean addAll(Collection c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean removeAll(Collection c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean retainAll(Collection c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void clear() {
        for (int i = 0; i < wrapped.length(); i++) {
            wrapped.remove(i);
        }
    }

    public Object[] toArray(Object[] a) {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object getAt(int i) {
        return wrap(wrapped.opt(i));
    }

    static Object wrap(Object o) {
        if (o instanceof CharSequence) {
            return o.toString();
        }
        if (o instanceof Collection) {
            return wrap((Collection) o);
        }
        if (o instanceof org.json.JSONArray) {
            return new org.json.JSONArray((org.json.JSONArray) o);
        }
        return o;
    }

    static Collection wrap(Collection c) {
        List list = new ArrayList(c.size());
        for (Iterator i = c.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof CharSequence) {
                list.add(o.toString());
            } else if (o instanceof Collection) {
                list.add(wrap((Collection) o));
            } else {
                list.add(o);
            }
        }
        return list;
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

}

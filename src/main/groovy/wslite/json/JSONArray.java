package wslite.json;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class JSONArray implements Collection {

    private org.json.JSONArray wrapped;

    public JSONArray() {
        wrapped = new org.json.JSONArray();
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

    public JSONArray(org.json.JSONArray jsonArray) {
        wrapped = jsonArray;
    }

    public int size() {
        return wrapped.length();
    }

    public boolean isEmpty() {
        return wrapped.length() == 0;
    }

    public boolean contains(Object o) {
        return toList().contains(wrap(o));
    }

    public Iterator iterator() {
        return toList().iterator();
    }

    public Object[] toArray() {
        return toList().toArray();
    }

    private List toList() {
        List list = new ArrayList(size());
        for (int i = 0; i < size(); i++) {
            list.add(getAt(i));
        }
        return list;
    }

    private int indexOf(Object o, int start) {
        int len = size();
        if (start >= len) {
            return -1;
        }
        for (int i = start; i < len; i++) {
            Object v = getAt(i);
            if (v == null && o == null) {
                return i;
            }
            if (!(v != null && o != null)) {
                continue;
            }
            if (wrap(v).equals(wrap(o))) {
                return i;
            }
        }
        return -1;
    }

    public boolean add(Object o) {
        return wrapped.put(o) != null;
    }

    public boolean remove(Object o) {
        boolean found = false;
        int index = -1;
        List<Integer> indexes = new ArrayList<Integer>();
        while ((index = indexOf(o, index + 1)) != -1) {
            indexes.add(index);
            found = true;
        }
        for (int i = indexes.size() - 1; i >= 0; i--) {
            wrapped.remove(indexes.get(i).intValue());
        }
        return found;
    }

    public boolean containsAll(Collection c) {
        return toList().containsAll(c);
    }

    public boolean addAll(Collection c) {
        if (c == null) {
            return false;
        }
        for (Object o : c) {
            wrapped.put(o);
        }
        return true;
    }

    public boolean removeAll(Collection c) {
        if (c == null) {
            return true;
        }
        boolean allRemoved = true;
        for (Object o : c) {
            if (!remove(o)) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    public boolean retainAll(Collection c) {
        if (c == null) {
            clear();
            return true;
        }
        boolean found = false;
        Object[] array = toArray();
        List<Integer> indexes = new ArrayList();
        for (int i = 0; i < array.length; i++) {
            Object o = getAt(i);
            if (!c.contains(o)) {
                indexes.add(i);
                found = true;
            }
        }
        for (Integer o : indexes) {
            wrapped.remove(o.intValue());
        }
        return found;
    }

    public void clear() {
        if (isEmpty()) {
            return;
        }
        for (int i = (size() - 1); i >= 0; i--) {
            wrapped.remove(i);
        }
    }

    public Object[] toArray(Object[] a) {
        return toList().toArray(a);
    }

    public Object getAt(int i) {
        return wrap(wrapped.opt(i));
    }

    static Object wrap(Object o) {
        if (o instanceof wslite.json.JSONArray) {
            return o;
        }
        if (o instanceof wslite.json.JSONObject) {
            return o;
        }
        if (o instanceof CharSequence) {
            return o.toString();
        }
        if (o instanceof Collection) {
            return wrap((Collection) o);
        }
        if (o instanceof org.json.JSONArray) {
            return new wslite.json.JSONArray((org.json.JSONArray) o);
        }
        if (o instanceof org.json.JSONObject) {
            return new wslite.json.JSONObject((org.json.JSONObject) o);
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

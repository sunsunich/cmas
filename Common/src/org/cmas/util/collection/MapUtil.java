package org.cmas.util.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;


public final class MapUtil {

    private MapUtil() {
    }

    /**
     * возвращает последние elements из списка collection,
     * если в нем столько нет, возвращает сколько есть.
     */
    public static <T> List<T> getLastNElements(Collection<T> collection, int elements) {
        if(collection.size() <= elements) {
            return new ArrayList<T>(collection);
        }
        List<T> result = new ArrayList<T>();
        int index = 0;
        for (T elem: collection) {
            if (index >= collection.size() - elements) {
                result.add(elem);
            }
            index++;
        }
        return result;
    }

    /*
    * Складывает значения двух Map'ов <key, Number>:  map1(key) = map1(key) + map2(key)
     */
    public static <K> void summ(Map<K, Long> res, Map<K, Long> addendum) {
        for (Map.Entry<K, Long> add : addendum.entrySet()) {
            K key = add.getKey();
            Long resValue = res.get(key);
            if (resValue == null) {
                resValue = 0L;
            }
            resValue += add.getValue();
            res.put(key, resValue);
        }
    }

    /**
     * создает Map по массивам ключей и значений
     * метод переехал сюда из client_ui, потому что тут мне больше нравится
     * @param keys
     * @param values
     * @return
     */
    public static <K,V> Map<K,V> map(K[] keys, V[] values){
        if (keys==null|| values == null|| keys.length!=values.length)
            throw new IllegalArgumentException("wtf? k="+keys.length+" v="+values.length);
        Map<K,V> res= new HashMap<K,V>();
        for (int i=0;i<keys.length;i++)
        res.put(keys[i],values[i]);
        return res;
    }

    public static <T> Set<T> set(T... values) {
        Set<T> res = new HashSet<T>();
        res.addAll(Arrays.asList(values));
        return res;
    }// возвращает значение по ключу и добавляет в хеш, если его не было.
    // последний параметр из-за erasure создается снаружи фукнции.
    public static <V,K> V getOrCreate(ConcurrentMap<K, V> map, K key, V newValue) {
        V value = map.get(key);
        if (value == null) {
            value = newValue;
            V oldValue = map.putIfAbsent(key, value);
            if (oldValue != null)
                value = oldValue;
        }
        return value;
    }
}

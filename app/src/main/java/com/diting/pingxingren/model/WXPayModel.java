package com.diting.pingxingren.model;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 19, 15:32.
 * Description: .
 */

public class WXPayModel<K, V> {

    private K key;
    private V value;

    public WXPayModel(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}

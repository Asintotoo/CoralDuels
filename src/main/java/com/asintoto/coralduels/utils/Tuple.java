package com.asintoto.coralduels.utils;

public class Tuple<T, V>{
    private T t;
    private V v;

    public Tuple(T t, V v) {
        this.t = t;
        this.v = v;
    }

    public Tuple() {
        this(null, null);
    }

    public T getFirst() {
        return t;
    }

    public V getSecond() {
        return v;
    }

    public void setFirst(T t) {
        this.t = t;
    }

    public void setSecond(V v) {
        this.v = v;
    }

    public boolean hasFirst() {
        return this.t != null;
    }

    public boolean hasSecond() {
        return this.v != null;
    }
}

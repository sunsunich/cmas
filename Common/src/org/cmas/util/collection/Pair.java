package org.cmas.util.collection;

public class Pair<F,S> {

    public Pair() {
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F first;

    public S second;

}

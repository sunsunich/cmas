package org.cmas.util;

public class LabelValue<T> {

    public final String label;
    public final T value;

    public LabelValue(String label, T value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public String toString() {
        return label;
    }
}

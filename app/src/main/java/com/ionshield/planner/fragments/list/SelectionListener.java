package com.ionshield.planner.fragments.list;

public interface SelectionListener<T> {
    void onSelect(T selected, SelectionMode mode);
}

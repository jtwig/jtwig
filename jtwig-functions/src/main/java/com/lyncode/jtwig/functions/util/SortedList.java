package com.lyncode.jtwig.functions.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

import static java.util.Collections.binarySearch;
import static java.util.Collections.sort;

public class SortedList<T extends Comparable<T>> extends LinkedList<T> {
    private final Comparator<T> comparator;

    public SortedList() {
        comparator = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.compareTo(o2);
            }
        };
    }

    @Override
    public boolean add(T paramT) {
        int insertionPoint = binarySearch(this, paramT, comparator);
        super.add((insertionPoint > -1) ? insertionPoint : (-insertionPoint) - 1, paramT);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> paramCollection) {
        boolean result = false;
        if (paramCollection.size() > 4) {
            result = super.addAll(paramCollection);
            sort(this, comparator);
        }
        else {
            for (T paramT:paramCollection) {
                result |= add(paramT);
            }
        }
        return result;
    }

    public boolean containsElement(T paramT) {
        return (binarySearch(this, paramT, comparator) > -1);
    }
}

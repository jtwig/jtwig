package com.lyncode.jtwig.functions.util;

import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SortedListTest {
    @Test
    public void sortsAddedElements() throws Exception {
        Collection<Integer> list = new SortedList<>();

        list.add(2);
        list.add(1);

        assertThat(list, contains(equalTo(1), equalTo(2)));
    }

    @Test
    public void sortsMultipleAddedElementsLessThan4() throws Exception {
        Collection<Integer> list = new SortedList<>();

        list.addAll(asList(2, 1));

        assertThat(list, contains(equalTo(1), equalTo(2)));
    }

    @Test
    public void sortsMultipleAddedElementsMoreThan4() throws Exception {
        Collection<Integer> list = new SortedList<>();

        list.addAll(asList(2, 1, 3, 4, 0));

        assertThat(list, contains(equalTo(0), equalTo(1), equalTo(2), equalTo(3), equalTo(4)));
    }

    @Test
    public void containsElement() throws Exception {
        Collection<Integer> list = new SortedList<>();

        list.addAll(asList(200));

        assertTrue(list.contains(200));
        assertFalse(list.contains(1));
    }
}
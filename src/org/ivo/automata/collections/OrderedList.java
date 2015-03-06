package org.ivo.automata.collections;

import java.util.ArrayList;

/**
 * Orders it's elements in ascending order on insert.
 * 
 * @author ivo
 * @param <E>
 */
public class OrderedList<E extends Comparable<E>> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;

    public OrderedList() {
        super();
    }

    @Override
    public boolean add(final E elem) {
        if (elem == null) {
            return false;
        }
        int index = 0;
        for (int i = 0; i < size(); i++) {
            final E next = get(i);
            final int comparison = elem.compareTo(next);
            if (comparison > 0) {
                index++;
            } else if (comparison == 0) {
                set(index, elem);
                return true;
            } else {
                break;
            }
        }
        super.add(index, elem);
        return true;
    }
}

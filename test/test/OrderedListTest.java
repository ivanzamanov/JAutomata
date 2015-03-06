package test;

import java.util.List;

import junit.framework.TestCase;

import org.ivo.automata.collections.OrderedList;

public class OrderedListTest extends TestCase {

    public void test() {
        final List<Integer> list = new OrderedList<Integer>();

        for (int i = 0; i < 200; i++) {
            list.add(200 - i - 1);
        }

        for (int i = 1; i < 200; i++) {
            assertTrue(list.get(i - 1) <= list.get(i));
        }

        for (int i = 0; i < 200; i++) {
            assertEquals(Integer.valueOf(i), list.get(i));
        }

        for (int i = 0; i < 200; i++) {
            assertEquals(i, list.indexOf(i));
        }
    }
}

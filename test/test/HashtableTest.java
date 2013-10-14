package test;

import java.util.LinkedList;

import junit.framework.TestCase;

import org.ivo.automata.State;
import org.ivo.automata.collections.StatesTable;

public class HashtableTest extends TestCase {
    
    private static final int NUM = 1000000;
    
    public void test() {
        final StatesTable table = new StatesTable(0.01f);
        final LinkedList<State> list = new LinkedList<State>();
        for (int i = 1; i <= NUM; i++) {
            final State state = new State();
            state.setIndex(i);
            list.addLast(state);
            table.addOrGet(state);
        }
        
        table.printDistribution();
        int i = 0;
        while (!list.isEmpty()) {
            assertTrue("Not found " + i, table.remove(list.removeFirst()));
            i++;
        }
        assertEquals(i, NUM);
        assertEquals(0, table.size());
    }
}

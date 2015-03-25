package test;

import junit.framework.TestCase;

import org.ivo.automata.State;

public class StateTest extends TestCase {
    
    public void testSingleState() {
        final State state = new State();
        state.setIndex(0);
        final int NUM = 2;
        for (int i = 0; i < NUM; i++) {
            state.addTransition(state.getIndex(), i);
        }
        
        for (int i = 0; i < NUM; i++) {
            assertNotNull(state.getTransitionTarget((char) i));
        }
    }
}

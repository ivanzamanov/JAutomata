package org.ivo.automata;

import java.io.Reader;

public interface IDeterministicAutomaton {
    State getStartState();
    
    boolean traverse(final Reader reader);
    
    State nextState(final State fromState, final int withChar);
    
    int getNumStates();
    
    State[] getStates();
}
